package com.gestion.tienda.tcg.carrito.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.tienda.tcg.carrito.model.CarritoItem;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
        // =========================
        // Metodo para listar los carritos segun su id en la entidad
        // =========================
        List<CarritoItem> findByCarrito_IdCarrito(Long idCarrito);

        // =========================
        // Metodo para validar produtos en carrito y que estos se sumen entre si, en vez
        // de duplicarse
        // =========================
        Optional<CarritoItem> findByCarrito_IdCarritoAndProductoId(
                        Long idCarrito,
                        Long productoId);

        // =========================
        // Metodo para buscar un item especifico dentro de un carrito especifico
        // =========================
        Optional<CarritoItem> findByCarrito_IdCarritoAndIdItem(
                        Long idCarrito,
                        Long idItem);
}
