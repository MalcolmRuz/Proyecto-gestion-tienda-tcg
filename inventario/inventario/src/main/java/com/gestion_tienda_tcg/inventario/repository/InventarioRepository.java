package com.gestion_tienda_tcg.inventario.repository;

import com.gestion_tienda_tcg.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository  extends JpaRepository <Inventario,Long> {
}
