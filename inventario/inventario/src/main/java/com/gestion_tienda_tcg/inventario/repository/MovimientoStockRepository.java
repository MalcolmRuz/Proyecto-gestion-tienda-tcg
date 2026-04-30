package com.gestion_tienda_tcg.inventario.repository;

import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoStockRepository extends JpaRepository <MovimientoStock,Long> {
    List<MovimientoStock> findByInventario_IdInventario(Long idInventario);
    List<MovimientoStock> findByTipo(TipoMovimiento tipo);
    List<MovimientoStock> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
