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
@Tag(name = "Pagos", description = "Gestión de pagos asociados a los pedidos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    @Operation(summary = "Realizar Pago", description = "Registro de pago para carrito pendiente de validación")
    public ResponseEntity<PagoResponse> crearPago(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.realizarPago(request));
    }

    @GetMapping
    @Operation(summary = "Lista pagos", description = "Listado de todos los pagos registrados")
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
            summary = "Listar pagos por estado", description = "Muestra todos los pagos según su estado de pago")
    public ResponseEntity<List<PagoResponse>> listarPagosPorEstado(
            @PathVariable EstadoPago estado){

        return ResponseEntity.ok(
                pagoService.listarPagosPorEstado(estado)
        );

    }

}