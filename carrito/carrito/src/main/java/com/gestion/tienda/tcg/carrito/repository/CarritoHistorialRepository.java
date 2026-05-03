package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;

public interface CarritoHistorialRepository extends JpaRepository<CarritoHistorial, Long> {
    List<CarritoHistorial> findByCarrito_idCarrito(Long idCarrito);

    // Obtener historial del carrito ordenado
    List<CarritoHistorial> findByCarritoIdCarritoOrderByFechaDesc(Long idCarrito);
}