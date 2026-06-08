package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // =========================
    // Metodo para listar los carritos, segun el Estado en que se encuentra
    // =========================

    List<Carrito> findByEstadoCarrito(EstadoCarrito estadoCarrito);
}
