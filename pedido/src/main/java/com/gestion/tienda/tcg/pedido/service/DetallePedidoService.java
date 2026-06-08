package com.gestion.tienda.tcg.pedido.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedido.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedido.mapper.DetallePedidoMapper;
import com.gestion.tienda.tcg.pedido.repository.DetallePedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    private final DetallePedidoRepository repository;
    private final DetallePedidoMapper mapper;

    public List<DetallePedidoResponse> listarPorPedido(Long pedidoId) {
        return repository.findByPedidoId(pedidoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}