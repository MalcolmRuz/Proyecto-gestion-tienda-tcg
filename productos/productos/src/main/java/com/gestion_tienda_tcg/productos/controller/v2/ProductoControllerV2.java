package com.gestion_tienda_tcg.productos.controller.v2;

import com.gestion_tienda_tcg.productos.assemblers.ProductoModelAssembler;
import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/productos")
@Tag(name = "PRODUCTO V2", description = "API HATEOAS para la gestión y ciclo de vida de productos usando EntityModel")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    @Operation(summary = "Creación de productos", description = "Crea un producto, inicializa su inventario y retorna hipermedios.")
    @PostMapping
    public ResponseEntity<EntityModel<ProductoResponse>> guardarProducto(@RequestBody ProductoRequest request) {
        log.info("REST request V2 para guardar producto: {}", request.getNombreProducto());
        var response = productoService.guardarProducto(request);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Edición de productos", description = "Modifica un producto según su ID y retorna el recurso actualizado con enlaces.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoResponse>> modificarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequest request) {
        log.info("REST request V2 para modificar producto ID: {}", id);
        var response = productoService.modificarProducto(id, request);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @Operation(summary = "Búsqueda de producto", description = "Retorna la información detallada de un producto con sus links de acción.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoResponse>> obtenerDetalle(@PathVariable Long id) {
        log.info("REST request V2 para obtener detalle del producto ID: {}", id);
        var response = productoService.obtenerDetalle(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @Operation(summary = "Desactivación de un producto", description = "Cambia el estado del producto a INACTIVO. No retorna cuerpo (204 No Content).")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
        log.info("REST request V2 para desactivar producto ID: {}", id);
        productoService.desactivarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listado de productos según proveedor")
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponse>>> listarPorProveedor(@PathVariable Long idProveedor) {
        log.info("REST request V2 para listar productos del proveedor ID: {}", idProveedor);
        var lista = productoService.listarPorProveedor(idProveedor);

        var collectionModel = assembler.toCollectionModel(lista)
                .add(linkTo(methodOn(ProductoControllerV2.class).listarPorProveedor(idProveedor)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}