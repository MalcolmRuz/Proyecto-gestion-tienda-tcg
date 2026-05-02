package com.gestion.tienda.tcg.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.carrito.model.Carrito;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

}
