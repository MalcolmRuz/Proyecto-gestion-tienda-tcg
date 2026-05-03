package com.gestion.tienda.tcg.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedidos.model.HistorialPedido;

public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {

    List<HistorialPedido> findByPedidoIdPedido(Long idPedido);

    List<HistorialPedido> findByEstadoPedido(EstadoPedido estadoPedido);
}