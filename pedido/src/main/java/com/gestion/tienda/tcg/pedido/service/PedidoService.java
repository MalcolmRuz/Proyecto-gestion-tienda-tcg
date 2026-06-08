package com.gestion.tienda.tcg.pedido.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedido.Client.CarritoClient;
import com.gestion.tienda.tcg.pedido.dto.CarritoDto;
import com.gestion.tienda.tcg.pedido.dto.CarritoItemDto;
import com.gestion.tienda.tcg.pedido.dto.EnvioRequest;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.exception.BadRequestException;
import com.gestion.tienda.tcg.pedido.exception.PedidoNotFoundException;
import com.gestion.tienda.tcg.pedido.mapper.EnvioMapper;
import com.gestion.tienda.tcg.pedido.mapper.PedidoMapper;
import com.gestion.tienda.tcg.pedido.model.DetallePedido;
import com.gestion.tienda.tcg.pedido.model.Envio;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;
import com.gestion.tienda.tcg.pedido.model.Pedido;
import com.gestion.tienda.tcg.pedido.repository.DetallePedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.EnvioRepository;
import com.gestion.tienda.tcg.pedido.repository.HistorialPedidoRepository;
import com.gestion.tienda.tcg.pedido.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

        private final PedidoRepository pedidoRepository;
        private final EnvioRepository envioRepository;

        private final DetallePedidoRepository detallePedidoRepository;
        private final HistorialPedidoRepository historialPedidoRepository;

        private final PedidoMapper pedidoMapper;
        private final EnvioMapper envioMapper;

        private final CarritoClient carritoClient;

        // ================================
        // CREAR PEDIDO DESDE CARRITO
        // ================================
        @Transactional
        public PedidoResponse crearPedido(Long idCarrito, Long usuarioId, EnvioRequest envioRequest) {
                log.info("Iniciando conversion automatica: Carrito {} -> Pedido", idCarrito);

                // 1. Obtener Carrito (Debe estar PAGADO)
                CarritoDto carrito = carritoClient.obtenerCarrito(idCarrito);

                if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
                        throw new BadRequestException("No se puede procesar un carrito vacio");
                }

                if (!"PAGADO".equalsIgnoreCase(carrito.getEstadoCarrito())) {
                        throw new BadRequestException("El carrito aun no ha sido pagado");
                }

                // FIX: calcular detalles y total ANTES del primer save para evitar
                // persistir el pedido en estado incompleto (totalPedido=0, sin detalles)
                // y eliminar el doble save innecesario.

                // 2. Construir detalles y calcular total
                List<DetallePedido> detalles = new ArrayList<>();
                double total = 0.0;

                for (CarritoItemDto item : carrito.getItems()) {
                        DetallePedido detalle = new DetallePedido();
                        detalle.setProductoId(item.getProductoId());
                        // FIX: era item.getNombreProducto() → siempre null.
                        // El campo real del CarritoItemResponse es "descripcionProducto".
                        detalle.setNombreProducto(item.getDescripcionProducto());
                        detalle.setCantidad(item.getCantidad());
                        detalle.setPrecioUnitario(item.getPrecioUnitario());

                        double subtotal = item.getPrecioUnitario() * item.getCantidad();
                        detalle.setPrecioTotal(subtotal);
                        total += subtotal;

                        detalles.add(detalle);
                }

                // 3. Crear Pedido con el total ya calculado y persistir una sola vez
                Pedido pedido = new Pedido();
                pedido.setUsuarioId(usuarioId);
                pedido.setCarritoId(idCarrito);
                pedido.setEstado(EstadoPedido.PAGADO);
                pedido.setTotalPedido(total);

                Pedido guardado = pedidoRepository.save(pedido);

                // 4. Asociar pedido a los detalles y guardarlos
                detalles.forEach(d -> d.setPedido(guardado));
                detallePedidoRepository.saveAll(detalles);
                guardado.setDetalles(detalles);

                // 5. Gestion de Envio e Historial
                Envio envio = envioMapper.toEntity(envioRequest);
                envio.setPedido(guardado);
                envioRepository.save(envio);

                HistorialPedido historial = new HistorialPedido();
                historial.setPedido(guardado);
                historial.setEstadoPedido(EstadoPedido.PAGADO);
                historial.setDescripcion("Pedido generado automaticamente tras confirmacion de pago");
                historialPedidoRepository.save(historial);

                return pedidoMapper.toResponse(guardado);
        }

        // ================================
        // LISTAR TODOS
        // ================================
        public List<PedidoResponse> listar() {

                return pedidoRepository.findAll()
                                .stream()
                                .map(pedidoMapper::toResponse)
                                .toList();
        }

        // ================================
        // BUSCAR POR ID
        // ================================
        public PedidoResponse buscarPorId(
                        @NonNull Long id) {

                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new PedidoNotFoundException(
                                                "Pedido no encontrado"));

                return pedidoMapper.toResponse(pedido);
        }

        // ================================
        // LISTAR POR USUARIO
        // ================================
        public List<PedidoResponse> listarPorUsuario(
                        Long usuarioId) {

                return pedidoRepository
                                .findByUsuarioId(usuarioId)
                                .stream()
                                .map(pedidoMapper::toResponse)
                                .toList();
        }

        // ================================
        // ACTUALIZAR ESTADO
        // ================================
        @Transactional
        public PedidoResponse actualizarEstado(
                        @NonNull Long id,
                        EstadoPedido estado) {

                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new PedidoNotFoundException(
                                                "Pedido no encontrado"));

                EstadoPedido estadoActual = pedido.getEstado();

                // VALIDAR TERMINALES
                if (estadoActual == EstadoPedido.CANCELADO
                                || estadoActual == EstadoPedido.FINALIZADO) {

                        throw new BadRequestException(
                                        "El pedido ya no puede cambiar de estado");
                }

                // VALIDACIONES
                if (estadoActual == EstadoPedido.PAGADO
                                && estado != EstadoPedido.PROCESANDO
                                && estado != EstadoPedido.CANCELADO) {

                        throw new BadRequestException(
                                        "Un pedido PAGADO solo puede pasar a PROCESANDO o CANCELADO");
                }

                if (estadoActual == EstadoPedido.PROCESANDO
                                && estado != EstadoPedido.FINALIZADO
                                && estado != EstadoPedido.CANCELADO) {

                        throw new BadRequestException(
                                        "Un pedido PROCESANDO solo puede pasar a FINALIZADO o CANCELADO");
                }

                pedido.setEstado(estado);

                // HISTORIAL
                HistorialPedido historial = new HistorialPedido();

                historial.setPedido(pedido);

                historial.setEstadoPedido(estado);

                historial.setDescripcion(
                                "Estado actualizado a "
                                                + estado.name());

                historialPedidoRepository.save(historial);

                // ENVIO
                if (pedido.getEnvio() != null) {

                        switch (estado) {

                                case PROCESANDO ->
                                        pedido.getEnvio().setEstadoEnvio(
                                                        EstadoEnvio.PREPARANDO);

                                case FINALIZADO ->
                                        pedido.getEnvio().setEstadoEnvio(
                                                        EstadoEnvio.EN_CAMINO);

                                case CANCELADO ->
                                        pedido.getEnvio().setEstadoEnvio(
                                                        EstadoEnvio.CANCELADO);

                                default -> {
                                }
                        }
                }

                pedidoRepository.save(pedido);

                return pedidoMapper.toResponse(pedido);
        }

        // ================================
        // MARCAR ENTREGADO
        // ================================
        @Transactional
        public PedidoResponse marcarEntregado(Long id) {

                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new PedidoNotFoundException(
                                                "Pedido no encontrado"));

                if (pedido.getEnvio() == null) {

                        throw new BadRequestException(
                                        "El pedido no tiene envío");
                }

                if (pedido.getEstado() != EstadoPedido.FINALIZADO
                                || pedido.getEnvio().getEstadoEnvio() != EstadoEnvio.EN_CAMINO) {

                        throw new BadRequestException(
                                        "El pedido debe estar FINALIZADO y EN_CAMINO");
                }

                pedido.getEnvio().setEstadoEnvio(
                                EstadoEnvio.ENTREGADO);

                pedidoRepository.save(pedido);

                return pedidoMapper.toResponse(pedido);
        }
}