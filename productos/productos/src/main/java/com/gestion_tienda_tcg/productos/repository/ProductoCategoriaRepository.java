package com.gestion_tienda_tcg.productos.repository;

import com.gestion_tienda_tcg.productos.model.ProductoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria,Long> {
    List<ProductoCategoria> findByCategoriaId(Long idCategoria);


    List<ProductoCategoria> findByProductoId(Long idProducto);
}
