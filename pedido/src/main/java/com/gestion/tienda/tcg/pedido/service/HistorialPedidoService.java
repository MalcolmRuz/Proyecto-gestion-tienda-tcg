package com.gestion.tienda.tcg.pedido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedido.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedido.mapper.HistorialPedidoMapper;
import com.gestion.tienda.tcg.pedido.repository.HistorialPedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialPedidoService {

    private final HistorialPedidoRepository repository;
    private final HistorialPedidoMapper mapper;

    public List<HistorialPedidoResponse> obtenerHistorial(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}