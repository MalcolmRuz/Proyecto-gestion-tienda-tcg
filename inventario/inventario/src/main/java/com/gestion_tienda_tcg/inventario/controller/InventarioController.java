package com.gestion_tienda_tcg.inventario.controller;

import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/inventarios")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<InventarioResponse> crearInventario(@Valid @RequestBody InventarioRequest request){
        InventarioResponse response = inventarioService.agregar(request);

        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping ("/producto/{idProducto}")
    public ResponseEntity<InventarioResponse> stockPorProducto(@Valid @PathVariable Long idProducto){
        InventarioResponse response = inventarioService.obtenerStockPorProducto(idProducto);
        return ResponseEntity.ok(response);


    }

    @PutMapping ("/{idProducto}/aumentar/{cantidad}")
    public ResponseEntity<InventarioResponse> aumentarStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.aumentarStock(idProducto,cantidad);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idProducto}/reducir/{cantidad}")
    public ResponseEntity<InventarioResponse> disminuirStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.disminuirStock(idProducto,cantidad);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<InventarioResponse> ajustarStock(@RequestBody InventarioRequest request){
        InventarioResponse response = inventarioService.ajustarStockFisico(request);
        return ResponseEntity.ok(response);
    }
}
