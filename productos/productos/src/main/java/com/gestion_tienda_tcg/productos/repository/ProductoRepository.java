package com.gestion_tienda_tcg.productos.repository;

import com.gestion_tienda_tcg.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    List<Producto> findByProveedor_IdProveedor(Long idProveedor);
    @Modifying
    @Query("UPDATE Producto p SET p.estadoActivo = false WHERE p.idProducto = :id")
    void desactivarProducto(@Param("id") Long id);
}
