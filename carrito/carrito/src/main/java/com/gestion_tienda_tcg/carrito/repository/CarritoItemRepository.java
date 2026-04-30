package com.gestion_tienda_tcg.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion_tienda_tcg.carrito.model.CarritoItem;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

}
