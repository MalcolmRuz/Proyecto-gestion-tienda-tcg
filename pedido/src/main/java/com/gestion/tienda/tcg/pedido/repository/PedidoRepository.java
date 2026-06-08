package com.gestion.tienda.tcg.pedido.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.pedido.enums.EstadoPedido;
import com.gestion.tienda.tcg.pedido.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByEstado(EstadoPedido estado);

    Optional<Pedido> findByCarritoId(Long carritoId);

    List<Pedido> findByUsuarioIdAndEstado(
            Long usuarioId,
            EstadoPedido estado);
}