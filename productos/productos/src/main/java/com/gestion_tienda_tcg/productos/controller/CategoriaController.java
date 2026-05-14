package com.gestion_tienda_tcg.productos.controller;

import com.gestion_tienda_tcg.productos.dto.CategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.service.CategoriaService;
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
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@RequestBody CategoriaRequest request) {
        log.info("REST request para crear una categoría: {}", request.getNombre());
        var response = categoriaService.crearCategoria(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        log.info("REST request para listar todas las categorías");
        var lista = categoriaService.listarCategorias();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> editarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequest request) {
        log.info("REST request para editar la categoría ID: {}", id);
        var response = categoriaService.editarCategoria(id, request);
        return ResponseEntity.ok(response);
    }
}

