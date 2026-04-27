package com.gestion_tienda_tcg.inventario.repository;

import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoStockRepository extends JpaRepository <MovimientoStock,Long> {
}
