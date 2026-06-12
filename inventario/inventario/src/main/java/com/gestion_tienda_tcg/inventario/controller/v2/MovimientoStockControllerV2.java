package com.gestion_tienda_tcg.inventario.controller.v2;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/movimientosStock")
@RequiredArgsConstructor
@Tag(name = "Movimiento Stock V2", description = "Historial de cambios en el stock en Inventario")
public class MovimientoStockControllerV2 {

}
