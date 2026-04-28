package com.gestion_tienda_tcg.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion_tienda_tcg.carrito.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

}
