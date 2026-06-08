package com.gestion.tienda.tcg.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.pedido.model.DetallePedido;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedidoId(Long pedidoId);
}