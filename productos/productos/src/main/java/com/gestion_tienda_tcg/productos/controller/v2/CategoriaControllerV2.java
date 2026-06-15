package com.gestion_tienda_tcg.productos.controller.v2;

import com.gestion_tienda_tcg.productos.assemblers.CategoriaModelAssembler;
import com.gestion_tienda_tcg.productos.dto.CategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/categoria")
@Tag(name = "CATEGORIA V2", description = "Control de categorias optimizada con HATEOAS")
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;

    @Operation(summary = "Obtener una categoría por ID")
    @GetMapping("/{id}")
    public EntityModel<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Long id) {
        log.info("REST request V2 para obtener categoría ID: {}", id);
        var response = categoriaService.obtenerCategoriaPorID(id);
        return assembler.toModel(response);
    }

    @Operation(summary = "Creación de categorías")
    @PostMapping
    public ResponseEntity<EntityModel<CategoriaResponse>> crearCategoria(@RequestBody CategoriaRequest request) {
        log.info("REST request V2 para crear una categoría: {}", request.getNombre());
        var response = categoriaService.crearCategoria(request);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todas las categorías")
    @GetMapping
    public CollectionModel<EntityModel<CategoriaResponse>> listarCategorias() {
        log.info("REST request V2 para listar todas las categorías");
        var lista = categoriaService.listarCategorias();
        return assembler.toCollectionModel(lista);
    }

    @Operation(summary = "Edición de una categoría")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CategoriaResponse>> editarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequest request) {
        log.info("REST request V2 para editar la categoría ID: {}", id);
        var response = categoriaService.editarCategoria(id, request);
        return ResponseEntity.ok(assembler.toModel(response));
    }


}
