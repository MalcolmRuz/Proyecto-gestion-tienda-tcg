package com.gestion.tienda.tcg.pedidos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.pedidos.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Optional<Envio> findByPedidoIdPedido(Long idPedido);
}
