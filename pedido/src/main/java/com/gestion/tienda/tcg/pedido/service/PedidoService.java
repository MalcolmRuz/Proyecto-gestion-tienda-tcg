package com.gestion.tienda.tcg.pedido.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.tienda.tcg.pedido.Client.CarritoClient;
import com.gestion.tienda.tcg.pedido.dto.CarritoDto;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.exception.BadRequestException;
import com.gestion.tienda.tcg.pedido.exception.PedidoNotFoundException;
import com.gestion.tienda.tcg.pedido.mapper.PedidoMapper;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;
import com.gestion.tienda.tcg.pedido.model.Pedido;
import com.gestion.tienda.tcg.pedido.repository.DetallePedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.HistorialPedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

        private final PedidoRepository pedidoRepository;
        private final DetallePedidoRepository detallePedidoRepository;
        private final HistorialPedidoRepository historialPedidoRepository;
        private final PedidoMapper pedidoMapper;
        private final CarritoClient carritoClient;

        @Transactional
        public PedidoResponse crearPedido(Long idCarrito, Long usuarioId, String direccionEnvio) {
                CarritoDto carrito = carritoClient.obtenerCarrito(idCarrito);
                if (!"PAGADO".equalsIgnoreCase(carrito.getEstadoCarrito())) {
                        throw new BadRequestException("El carrito no está pagado.");
                }

                Pedido pedido = new Pedido();
                pedido.setUsuarioId(usuarioId);
                pedido.setIdCarrito(idCarrito);
                pedido.setEstado(EstadoPedido.PAGADO);
                pedido.setTotalPedido(carrito.getTotalCarrito());
                pedido.setDireccionEnvio(direccionEnvio);
                pedido.setEstadoEnvio(EstadoEnvio.PREPARANDO);

                Pedido guardado = pedidoRepository.save(pedido);

                List<DetallePedido> detalles = carrito.getItems().stream().map(item -> {
                        DetallePedido d = new DetallePedido();
                        d.setPedido(guardado);
                        d.setProductoId(item.getProductoId());
                        d.setNombreProducto(item.getDescripcionProducto());
                        d.setCantidad(item.getCantidad());
                        d.setPrecioUnitario(item.getPrecioUnitario());
                        d.setPrecioTotal(item.getPrecioUnitario() * item.getCantidad());
                        return d;
                }).collect(Collectors.toList());

                detallePedidoRepository.saveAll(detalles);

                HistorialPedido h = new HistorialPedido();
                h.setPedido(guardado);
                h.setEstadoPedido(EstadoPedido.PAGADO);
                h.setDescripcion("Pedido creado correctamente.");
                h.setFechaCambio(LocalDateTime.now());
                historialPedidoRepository.save(h);

                return pedidoMapper.toResponse(guardado);
        }

        @Transactional
        public PedidoResponse actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado: " + id));

                pedido.setEstado(nuevoEstado);
                Pedido actualizado = pedidoRepository.save(pedido);

                HistorialPedido h = new HistorialPedido();
                h.setPedido(actualizado);
                h.setEstadoPedido(nuevoEstado);
                h.setDescripcion("Estado actualizado a: " + nuevoEstado);
                h.setFechaCambio(LocalDateTime.now());
                historialPedidoRepository.save(h);

                return pedidoMapper.toResponse(actualizado);
        }

        public List<PedidoResponse> listarTodos() {
                return pedidoRepository.findAll().stream().map(pedidoMapper::toResponse).collect(Collectors.toList());
        }

        public PedidoResponse buscarPorId(Long id) {
                return pedidoRepository.findById(id).map(pedidoMapper::toResponse)
                                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado: " + id));
        }
}