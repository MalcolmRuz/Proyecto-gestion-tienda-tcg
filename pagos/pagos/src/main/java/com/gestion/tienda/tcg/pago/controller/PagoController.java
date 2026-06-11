package com.gestion.tienda.tcg.pago.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}