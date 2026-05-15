package com.gestion.tienda.tcg.pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedidos.dto.HistorialPedidoResponse;
import com.gestion.tienda.tcg.pedidos.mapper.HistorialPedidoMapper;
import com.gestion.tienda.tcg.pedidos.repository.HistorialPedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialPedidoService {

    private final HistorialPedidoRepository repository;
    private final HistorialPedidoMapper mapper;

    public List<HistorialPedidoResponse> listarPorPedido(Long idPedido) {

        return repository.findByPedidoIdPedido(idPedido)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}