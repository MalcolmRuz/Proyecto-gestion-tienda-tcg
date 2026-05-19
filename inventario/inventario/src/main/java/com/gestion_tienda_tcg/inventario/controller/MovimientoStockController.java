package com.gestion_tienda_tcg.inventario.controller;


import com.gestion_tienda_tcg.inventario.dto.MovimientoStockResponse;
import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.service.MovimientoStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/movimientosStock")
@RequiredArgsConstructor
public class MovimientoStockController {
    private final MovimientoStockService movimientoStockService;

@GetMapping
public ResponseEntity<List<MovimientoStockResponse>> listarHistorial(){
    return ResponseEntity.ok(movimientoStockService.listarHistorialCompleto());
}

@GetMapping("/{idInventario}")
public ResponseEntity<List<MovimientoStockResponse>> listarPorId(@PathVariable Long idInventario){
    return  ResponseEntity.ok(movimientoStockService.listarMovimientosPorInventario(idInventario));
}

    /**
     * Obtiene los movimientos de inventario filtrados por una fecha de inicio Y DE termino.
     *
     * @param inicio
     *
     * Fecha y hora inicial en formato ISO DATE_TIME.
     * @param fin
     * Fecha y hora final en formato ISO DATE_TIME.
     *               Ejemplo de formato: '2026-05-18T21:15:30' (AAAA-MM-DDTHH:mm:ss)
     */

    @GetMapping("/reportePorfecha")
    public ResponseEntity<List<MovimientoStockResponse>> obtenerReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(movimientoStockService.reporteEntreFechas(inicio, fin));
}

@GetMapping("/tipo")
    public ResponseEntity<List<MovimientoStockResponse>> listarPorTipo(@RequestParam TipoMovimiento tipo){
    return  ResponseEntity.ok(movimientoStockService.listarPorTipo(tipo));

}
}
