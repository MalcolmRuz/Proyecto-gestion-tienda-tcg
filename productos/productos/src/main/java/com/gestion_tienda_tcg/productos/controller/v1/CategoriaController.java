package com.gestion_tienda_tcg.productos.controller.v1;

import com.gestion_tienda_tcg.productos.dto.CategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categoria")
@Tag(name = "CATEGORIA", description = "API que maneja las categorias, tipos de productos TCG")
public class CategoriaController {
    private final CategoriaService categoriaService;
    @Operation(summary = "Creación de categorías", description = "Crea una categoría con un String de nombre y de tipo de Producto")
    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@RequestBody CategoriaRequest request) {
        log.info("REST request para crear una categoría: {}", request.getNombre());
        var response = categoriaService.crearCategoria(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "Lista todas las categorias", description = "Retorna una lista con todas las categorías creadas")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        log.info("REST request para listar todas las categorías");
        var lista = categoriaService.listarCategorias();
        return ResponseEntity.ok(lista);
    }
    @Operation(summary = "Edición de una categoría",description = "Busca una categoria a partir de su id y la edita a partir del JSON requerido")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> editarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequest request) {
        log.info("REST request para editar la categoría ID: {}", id);
        var response = categoriaService.editarCategoria(id, request);
        return ResponseEntity.ok(response);
    }
}

