package com.gestion.tienda.tcg.pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedidos.dto.EnvioRequest;
import com.gestion.tienda.tcg.pedidos.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedidos.exception.BadRequestException;
import com.gestion.tienda.tcg.pedidos.exception.PedidoNotFoundException;
import com.gestion.tienda.tcg.pedidos.mapper.EnvioMapper;
import com.gestion.tienda.tcg.pedidos.mapper.PedidoMapper;
import com.gestion.tienda.tcg.pedidos.model.Envio;
import com.gestion.tienda.tcg.pedidos.model.Pedido;
import com.gestion.tienda.tcg.pedidos.repository.EnvioRepository;
import com.gestion.tienda.tcg.pedidos.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EnvioRepository envioRepository;

    private final PedidoMapper pedidoMapper;
    private final EnvioMapper envioMapper;

    // ================================
    // CREAR PEDIDO DESDE CARRITO
    // ================================
    @Transactional
    public PedidoResponse crearPedido(
            Long idCarrito,
            Long usuarioId,
            EnvioRequest envioRequest) {

        log.info("Creando pedido desde carrito {}", idCarrito);

        // 🔹 Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setCarritoId(idCarrito);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotalPedido(0.0);

        Pedido guardado = pedidoRepository.save(pedido);

        // 🔹 Crear envío
        Envio envio = envioMapper.toEntity(envioRequest);
        envio.setPedido(guardado);

        envioRepository.save(envio);
        guardado.setEnvio(envio);

        // 🔹 (Temporal) total en 0 hasta conectar con carrito
        guardado.setTotalPedido(0.0);

        return pedidoMapper.toResponse(guardado);
    }

    // ================================
    // LISTAR TODOS
    // ================================
    public List<PedidoResponse> listar() {

        log.info("Listando pedidos");

        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    // ================================
    // BUSCAR POR ID
    // ================================
    public PedidoResponse buscarPorId(Long id) {

        log.info("Buscando pedido {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));

        return pedidoMapper.toResponse(pedido);
    }

    // ================================
    // LISTAR POR USUARIO
    // ================================
    public List<PedidoResponse> listarPorUsuario(Long usuarioId) {

        log.info("Listando pedidos del usuario {}", usuarioId);

        return pedidoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }

    // ================================
    // ACTUALIZAR ESTADO
    // ================================
    @Transactional
    public PedidoResponse actualizarEstado(Long id, EstadoPedido estado) {

        log.info("Actualizando estado del pedido {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));

        // 🔥 Regla de negocio
        if (pedido.getEstado() == EstadoPedido.FINALIZADO) {
            throw new BadRequestException("No se puede modificar un pedido finalizado");
        }

        pedido.setEstado(estado);

        return pedidoMapper.toResponse(pedido);
    }

    // ================================
    // ELIMINAR
    // ================================
    @Transactional
    public void eliminar(Long id) {

        log.warn("Eliminando pedido {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));

        if (pedido.getEstado() == EstadoPedido.FINALIZADO) {
            throw new BadRequestException("No se puede eliminar un pedido enviado");
        }

        pedidoRepository.delete(pedido);
    }
}