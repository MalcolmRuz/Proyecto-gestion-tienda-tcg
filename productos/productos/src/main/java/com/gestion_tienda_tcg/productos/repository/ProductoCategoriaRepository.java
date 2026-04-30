package com.gestion_tienda_tcg.productos.repository;

import com.gestion_tienda_tcg.productos.model.ProductoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria,Long> {
}
