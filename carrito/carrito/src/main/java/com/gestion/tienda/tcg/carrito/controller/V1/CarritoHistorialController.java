package com.gestion.tienda.tcg.carrito.controller.V1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoHistorialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos/historial")
@RequiredArgsConstructor
@Tag(name = "CarritoHistorialController", description = "Controlador para gestionar el historial de carritos de compras")
public class CarritoHistorialController {

        private final CarritoHistorialService historialService;

        // Obtener historial carrito
        @Operation(summary = "Obtener el historial de un carrito de compras", description = "Obtiene el historial de un carrito de compras específico utilizando su ID")
        @GetMapping("/{idCarrito}")
        public ResponseEntity<List<CarritoHistorialResponse>> obtenerHistorial(
                        @PathVariable @NonNull Long idCarrito) {

                log.info("Consultando historial carrito {}",
                                idCarrito);

                return ResponseEntity.ok(
                                historialService.obtenerHistorial(idCarrito));
        }
}
