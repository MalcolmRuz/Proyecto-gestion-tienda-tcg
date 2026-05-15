package com.gestion.tienda.tcg.pedidos.service;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedidos.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedidos.mapper.EnvioMapper;
import com.gestion.tienda.tcg.pedidos.model.Envio;
import com.gestion.tienda.tcg.pedidos.repository.EnvioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository repository;
    private final EnvioMapper mapper;

    public EnvioResponse obtenerPorPedido(Long idPedido) {

        Envio envio = repository.findByPedidoIdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));

        return mapper.toResponse(envio);
    }
}