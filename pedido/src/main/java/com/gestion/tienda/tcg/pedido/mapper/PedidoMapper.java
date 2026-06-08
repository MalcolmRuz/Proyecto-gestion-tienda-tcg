package com.gestion.tienda.tcg.pedido.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.CarritoDto;
import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.dto.PedidoRequest;
import com.gestion.tienda.tcg.pedido.dto.PedidoResponse;
import com.gestion.tienda.tcg.pedido.model.Pedido;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoMapper {

    private final DetallePedidoMapper detalleMapper;
    private final EnvioMapper envioMapper;
    private final HistorialPedidoMapper historialMapper;

    public Pedido toEntity(PedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.getUsuarioId());
        return pedido;
    }

    public Pedido cargarDatosDesdeCarrito(Pedido pedido, CarritoDto carritoDto) {
        if (carritoDto != null) {
            pedido.setCarritoId(carritoDto.getIdCarrito());
            pedido.setTotalPedido(carritoDto.getTotalCarrito());
        }
        return pedido;
    }

    public PedidoResponse toResponse(Pedido pedido) {

        List<DetallePedidoResponse> detalles = null;
        if (pedido.getDetalles() != null) {
            detalles = pedido.getDetalles()
                    .stream()
                    .map(detalleMapper::toResponse)
                    .collect(Collectors.toList());
        }

        List<HistorialPedidoResponse> historial = null;
        if (pedido.getHistorial() != null) {
            historial = pedido.getHistorial()
                    .stream()
                    .map(historialMapper::toResponse)
                    .collect(Collectors.toList());
        }

        EnvioResponse envio = null;
        if (pedido.getEnvio() != null) {
            envio = envioMapper.toResponse(pedido.getEnvio());
        }

        return new PedidoResponse(
                pedido.getIdPedido(),
                pedido.getUsuarioId(),
                pedido.getCarritoId(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotalPedido(),
                detalles,
                historial,
                envio);
    }
}