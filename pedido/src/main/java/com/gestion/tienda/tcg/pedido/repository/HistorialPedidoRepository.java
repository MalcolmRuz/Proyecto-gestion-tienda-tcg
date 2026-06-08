package com.gestion.tienda.tcg.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.pedido.model.HistorialPedido;

@Repository
public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {
    List<HistorialPedido> findByPedidoId(Long pedidoId);
}