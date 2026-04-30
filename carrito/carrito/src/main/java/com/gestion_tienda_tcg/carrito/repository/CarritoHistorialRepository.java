package com.gestion_tienda_tcg.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion_tienda_tcg.carrito.model.CarritoHistorial;

public interface CarritoHistorialRepository extends JpaRepository<CarritoHistorial, Long> {
    List<CarritoHistorial> findByCarrito_idCarrito(Long idCarrito);
}
