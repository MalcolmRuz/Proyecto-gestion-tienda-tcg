package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<CarritoHistorial> findByCarritoIdCarritoOrderByFechaDesc(Long idCarrito);

    // Buscar carritos por estado
    List<Carrito> findByEstadoCarrito(EstadoCarrito estadoCarrito);
}
