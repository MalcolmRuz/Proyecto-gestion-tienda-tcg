package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.carrito.model.CarritoItem;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    // Listar items por carrito
    List<CarritoItem> findByCarritoIdCarrito(Long idCarrito);

    // Validar producto repetido en carrito
    Optional<CarritoItem> findByCarritoIdCarritoAndProductoId(
            Long idCarrito,
            Long productoId);
}
