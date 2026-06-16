package com.gestion.tienda.tcg.pago.controller;

import java.util.List;

import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.service.PagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Crea un pago tipo boleta luego de un pedido aprobado")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    @Operation(summary = "Creación de Boleta", description = "Creación de una boleta luego de que un pedido haya sido aprobado")
    public ResponseEntity<PagoResponse> crearPago(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.realizarPago(request));
    }

    @GetMapping
    @Operation(summary = "Lista Boletas", description = "Listado de boletas de todos los pagos ya aprobados")
    public ResponseEntity<List<PagoResponse>> listarPagos() {

        return ResponseEntity.ok(pagoService.listarPagos());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pago por ID", description = "Obtiene la información de un pago mediante su ID")
    public ResponseEntity<PagoResponse> buscarPagoPorId(
            @PathVariable Long id){

        return ResponseEntity.ok(
                pagoService.buscarPagoPorId(id)
        );

    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Listar pagos por estado", description = "Obtiene todos los pagos según su estado")
    public ResponseEntity<List<PagoResponse>> listarPagosPorEstado(
            @PathVariable EstadoPago estado){

        return ResponseEntity.ok(
                pagoService.listarPagosPorEstado(estado)
        );

    }

}