package com.gestion_tienda_tcg.inventario.controller;

import com.gestion_tienda_tcg.inventario.dto.InventarioDetalleResponse;
import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/inventarios")
@RequiredArgsConstructor
@Tag(name = "INVENTARIO",description = "Gestion de inventario,stock,mantencion")
public class InventarioController {
    private final InventarioService inventarioService;

    @Operation(summary = "Obtener todos los Inventarios con su detalle", description = "Retorna una lista completa de los inventarios, captura datos desde PRODUCTO como nombre")
    @GetMapping("/simple")
    public ResponseEntity<List<InventarioDetalleResponse>> listarInventariosConProductos() {
        List<InventarioDetalleResponse> detalles = inventarioService.listarInventariosConProducto();

        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //HATEOAS
        for (InventarioDetalleResponse detalle : detalles) {
            Link productoLink = linkTo(methodOn(InventarioController.class).stockPorProducto(detalle.getIdProducto())).withSelfRel();
            detalle.add(productoLink);
        }
        return ResponseEntity.ok(detalles);
    }

    //SE CREA AUTOMATICO AL HACER UN PRODUCTO.
    @Operation(summary = "Creacion de inventario", description = "Crea un inventario, al crear un producto se llama este ENDPOINT para creado automatico")
    @PostMapping
    public ResponseEntity<InventarioResponse> crearInventario(@Valid @RequestBody InventarioRequest request){
        InventarioResponse response = inventarioService.agregar(request);
        //HATEOAS
        Link selfLink = linkTo(methodOn(InventarioController.class).stockPorProducto(response.getIdProducto())).withSelfRel();
        response.add(selfLink);

        Link allLink = linkTo(methodOn(InventarioController.class).listarInventariosConProductos()).withRel("todos-los-inventarios");
        response.add(allLink);
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Busqueda de Inventario segun ID", description = "Retorna Inventario segun ID, individual")
    @GetMapping ("/producto/{idProducto}")
    public ResponseEntity<InventarioResponse> stockPorProducto(@Valid @PathVariable Long idProducto){
        InventarioResponse response = inventarioService.obtenerStockPorProducto(idProducto);
        //HATEOAS
        Link selfLink = linkTo(methodOn(InventarioController.class).stockPorProducto(idProducto)).withSelfRel();
        response.add(selfLink);

        Link allLink = linkTo(methodOn(InventarioController.class).listarInventariosConProductos()).withRel("todos-los-inventarios");
        response.add(allLink);
        return ResponseEntity.ok(response);


    }
    //REQUIERE PERMISO DE ADMIN
    @Operation(summary = "Aumenta stock a un inventario", description = "Aumenta Stock de Inventario buscado según ID")
    @PutMapping ("/{idProducto}/aumentar/{cantidad}")
    public ResponseEntity<InventarioResponse> aumentarStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.aumentarStock(idProducto,cantidad);
        //SELF      HATEOAS
        Link selfLink = linkTo(methodOn(InventarioController.class).aumentarStock(idProducto, cantidad)).withSelfRel();
        response.add(selfLink);
        //VER STOCK
        Link consultaLink = linkTo(methodOn(InventarioController.class).stockPorProducto(response.getIdProducto())).withRel("ver-stock-actual");
        response.add(consultaLink);

        return ResponseEntity.ok(response);
    }
    //SOLO USUARIO AUTENTICADO
    @Operation(summary = "Disminuye stock a un inventario", description = "Disminuye Stock de Inventario buscado según ID")
    @PutMapping("/{idProducto}/reducir/{cantidad}")
    public ResponseEntity<InventarioResponse> disminuirStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.disminuirStock(idProducto,cantidad);
        //HATEOAS
        Link selfLink = linkTo(methodOn(InventarioController.class).disminuirStock(idProducto, cantidad)).withSelfRel();
        response.add(selfLink);

        Link consultaLink = linkTo(methodOn(InventarioController.class).stockPorProducto(response.getIdProducto())).withRel("ver-stock-actual");
        response.add(consultaLink);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Ajuste de  stock a un inventario", description = "Ajusta Stock de Inventario buscado según ID")
    @PutMapping
    public ResponseEntity<InventarioResponse> ajustarStock(@RequestBody @Valid InventarioRequest request){
        InventarioResponse response = inventarioService.ajustarStockFisico(request);
        //HATEOAS
        Link selfLink = linkTo(methodOn(InventarioController.class).ajustarStock(request)).withSelfRel();
        response.add(selfLink);

        Link consultaLink = linkTo(methodOn(InventarioController.class).stockPorProducto(response.getIdProducto())).withRel("ver-stock-actual");
        response.add(consultaLink);

        return ResponseEntity.ok(response);
    }



}
