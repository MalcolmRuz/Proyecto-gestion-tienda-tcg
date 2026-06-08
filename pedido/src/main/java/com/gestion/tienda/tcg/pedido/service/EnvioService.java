package com.gestion.tienda.tcg.pedido.service;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedido.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedido.enums.EstadoEnvio;
import com.gestion.tienda.tcg.pedido.exception.EnvioNotFoundException;
import com.gestion.tienda.tcg.pedido.mapper.EnvioMapper;
import com.gestion.tienda.tcg.pedido.model.Envio;
import com.gestion.tienda.tcg.pedido.repository.EnvioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository repository;
    private final EnvioMapper mapper;

    public EnvioResponse obtenerPorPedido(Long idPedido) {

        Envio envio = repository.findByPedidoIdPedido(idPedido)
                .orElseThrow(() -> new EnvioNotFoundException(
                        "Envío no encontrado"));

        return mapper.toResponse(envio);
    }

    @Transactional
    public EnvioResponse actualizarEstado(
            Long idPedido,
            EstadoEnvio estado) {

        Envio envio = repository.findByPedidoIdPedido(idPedido)
                .orElseThrow(() -> new EnvioNotFoundException(
                        "Envío no encontrado"));

        envio.setEstadoEnvio(estado);

        return mapper.toResponse(envio);
    }
}