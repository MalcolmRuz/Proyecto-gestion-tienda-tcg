package com.gestion.tienda.tcg.pedidos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.pedidos.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByEstado(EstadoPedido estado);

    Optional<Pedido> findByCarritoId(Long carritoId);

    List<Pedido> findByUsuarioIdAndEstado(
            Long usuarioId,
            EstadoPedido estado);
}