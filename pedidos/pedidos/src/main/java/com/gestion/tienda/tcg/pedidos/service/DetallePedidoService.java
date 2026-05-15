package com.gestion.tienda.tcg.pedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.pedidos.dto.DetallePedidoResponse;
import com.gestion.tienda.tcg.pedidos.mapper.DetallePedidoMapper;
import com.gestion.tienda.tcg.pedidos.repository.DetallePedidoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    private final DetallePedidoRepository repository;
    private final DetallePedidoMapper mapper;

    public List<DetallePedidoResponse> listarPorPedido(Long idPedido) {

        return repository.findByPedidoIdPedido(idPedido)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}