package com.gestion_tienda_tcg.inventario.controller;

import com.gestion_tienda_tcg.inventario.assemblers.InventarioDetalleModelAssembler;
import com.gestion_tienda_tcg.inventario.assemblers.InventarioModelAssembler;
import com.gestion_tienda_tcg.inventario.dto.InventarioDetalleResponse;
import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/inventarios")
@RequiredArgsConstructor
@Tag(name = "INVENTARIO V2", description = "Gestión de inventario optimizada con HATEOAS sin boilerplate")
public class InventarioControllerV2 {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;
    private final InventarioDetalleModelAssembler assemblerDetalle;

    @Operation(summary = "Obtener todos los Inventarios con su detalle", description = "Retorna CollectionModel directo. Spring infiere el 200 OK.")
    @GetMapping("/simple")
    public CollectionModel<EntityModel<InventarioDetalleResponse>> listarInventariosConProductos() {
        List<InventarioDetalleResponse> detalles = inventarioService.listarInventariosConProducto();

        // Si está vacía, Spring HATEOAS permite retornar una colección vacía con su link rel
        List<EntityModel<InventarioDetalleResponse>> coleccion = detalles.stream()
                .map(assemblerDetalle::toModel)
                .toList();

        return CollectionModel.of(coleccion,
                linkTo(methodOn(InventarioControllerV2.class).listarInventariosConProductos()).withSelfRel());
    }

    @Operation(summary = "Creación de inventario", description = "Aquí SÍ dejamos ResponseEntity para forzar el código HTTP 201 Created")
    @PostMapping
    public ResponseEntity<EntityModel<InventarioResponse>> crearInventario(@Valid @RequestBody InventarioRequest request){
        InventarioResponse response = inventarioService.agregar(request);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Búsqueda de Inventario según ID", description = "Retorna EntityModel directo. Chau ResponseEntity.")
    @GetMapping ("/producto/{idProducto}")
    public EntityModel<InventarioResponse> stockPorProducto(@Valid @PathVariable Long idProducto){
        InventarioResponse response = inventarioService.obtenerStockPorProducto(idProducto);
        return assembler.toModel(response);
    }

    @Operation(summary = "Aumenta stock a un inventario")
    @PutMapping ("/{idProducto}/aumentar/{cantidad}")
    public EntityModel<InventarioResponse> aumentarStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.aumentarStock(idProducto, cantidad);
        return assembler.toModel(response);
    }

    @Operation(summary = "Disminuye stock a un inventario")
    @PutMapping("/{idProducto}/reducir/{cantidad}")
    public EntityModel<InventarioResponse> disminuirStock(@PathVariable Long idProducto, @PathVariable Integer cantidad){
        InventarioResponse response = inventarioService.disminuirStock(idProducto, cantidad);
        return assembler.toModel(response);
    }

    @Operation(summary = "Ajuste de stock a un inventario")
    @PutMapping
    public EntityModel<InventarioResponse> ajustarStock(@RequestBody @Valid InventarioRequest request){
        InventarioResponse response = inventarioService.ajustarStockFisico(request);
        return assembler.toModel(response);
    }
}