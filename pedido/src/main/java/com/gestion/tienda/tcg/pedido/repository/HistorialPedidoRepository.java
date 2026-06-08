package com.gestion.tienda.tcg.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.model.HistorialPedido;

public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, Long> {

    List<HistorialPedido> findByPedidoIdPedido(Long idPedido);

    List<HistorialPedido> findByEstadoPedido(EstadoPedido estadoPedido);
}