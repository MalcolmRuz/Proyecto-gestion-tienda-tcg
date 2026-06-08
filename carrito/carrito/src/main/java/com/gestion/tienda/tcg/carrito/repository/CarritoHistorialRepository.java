package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;

public interface CarritoHistorialRepository extends JpaRepository<CarritoHistorial, Long> {

    // =========================
    // Metodo para listar carritos en el historial segun su id
    // =========================

    List<CarritoHistorial> findByCarrito_idCarrito(Long idCarrito);

    // =========================
    // Metodo para listar carritos en el historial segun su id, agregando la
    // condicion de ordenarlo de forma descendente por fecha
    // =========================

    List<CarritoHistorial> findByCarrito_idCarritoOrderByFechaDesc(Long idCarrito);
}
