package com.gestion.tienda.tcg.pago.controller;

import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crearPago(@Valid @RequestBody PagoRequest request){
        return ResponseEntity.ok(pagoService.realizarPago(request));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listarPagos(){

        return ResponseEntity.ok(pagoService.listarPagos());
    }

}