package com.gestion.tienda.tcg.carrito.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.service.CarritoHistorialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos/historial")
@RequiredArgsConstructor
public class CarritoHistorialController {

        private final CarritoHistorialService historialService;

        // Obtener historial carrito
        @GetMapping("/{idCarrito}")
        public ResponseEntity<List<CarritoHistorialResponse>> obtenerHistorial(
                        @PathVariable @NonNull Long idCarrito) {

                log.info("Consultando historial carrito {}",
                                idCarrito);

                return ResponseEntity.ok(
                                historialService.obtenerHistorial(idCarrito));
        }
}
