package com.gestion_tienda_tcg.inventario.controller;


import com.gestion_tienda_tcg.inventario.dto.MovimientoStockResponse;
import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.service.MovimientoStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/movimientosStock")
@RequiredArgsConstructor
@Tag(name = "MOVIMIENTOSTOCK",description = "Historial de movimientos de stock de Inventario")
public class MovimientoStockController {
    private final MovimientoStockService movimientoStockService;

    @Operation(summary = "Obtener todos los Inventarios con su detalle", description = "Retorna una lista completa del historial de movimientos de stock")
    @GetMapping
public ResponseEntity<List<MovimientoStockResponse>> listarHistorial(){
        List<MovimientoStockResponse> historial = movimientoStockService.listarHistorialCompleto();
        if (historial.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(historial);
}
    @Operation(summary = "Detalles de un Movimiento de stock", description = "Retorna un movimiento de stock buscado segun ID")
@GetMapping("/{idInventario}")
public ResponseEntity<List<MovimientoStockResponse>> listarPorId(@PathVariable Long idInventario){
        List<MovimientoStockResponse> movimientos = movimientoStockService.listarMovimientosPorInventario(idInventario);
        if (movimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(movimientos);
}
    @Operation(summary = "Genera Reporte segun fecha", description = "Obtiene los movimientos de inventario filtrados por una fecha de inicio Y DE termino")

    @GetMapping("/reportePorfecha")
    public ResponseEntity<List<MovimientoStockResponse>> obtenerReporte(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<MovimientoStockResponse> reporte = movimientoStockService.reporteEntreFechas(inicio, fin);

        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
}
    @Operation(summary = "Genera Reporte segun tipo Movimiento", description = "Retorna una lista de movimientos de inventario filtrados  según tipo de Movimiento")
@GetMapping("/tipo")

    public ResponseEntity<List<MovimientoStockResponse>> listarPorTipo(@RequestParam TipoMovimiento tipo){
        List<MovimientoStockResponse> movimientosPorTipo = movimientoStockService.listarPorTipo(tipo);

        if (movimientosPorTipo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(movimientosPorTipo);
}
}
