package com.gestion.tienda.tcg.carrito.repository;

import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem,Long> {
    //=========================
    //Metodo para listar los carritos segun su id en la entidad
    //=========================
    List<CarritoItem> findByCarrito_IdCarrito(Long idCarrito);

    //=========================
    //Metodo para validar produtos en carrito y que estos se sumen entre si, en vez de duplicarse
    //=========================
    Optional<CarritoItem> findByCarrito_IdCarritoAndProductoId(
            Long idCarrito,
            Long productoId);
}
