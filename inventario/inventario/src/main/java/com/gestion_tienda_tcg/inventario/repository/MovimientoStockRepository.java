package com.gestion_tienda_tcg.inventario.repository;

import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository <MovimientoStock,Long> {
    List<MovimientoStock> findByTipo(TipoMovimiento tipo);
    List<MovimientoStock> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
