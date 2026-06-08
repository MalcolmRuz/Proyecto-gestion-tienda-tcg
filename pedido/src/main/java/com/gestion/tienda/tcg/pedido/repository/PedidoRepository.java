package com.gestion.tienda.tcg.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.pedido.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
}