package com.gestion_tienda_tcg.inventario.repository;

import com.gestion_tienda_tcg.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository  extends JpaRepository <Inventario,Long> {
   //***** como idProducto no es la ID primaria de Inventario se debe crear este buscador ID
    Optional<Inventario> findByIdProducto(Long idProducto);
}
