package com.gestion_tienda_tcg.productos.service;


import com.gestion_tienda_tcg.productos.dto.CategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.exception.CategoriaInvalidaException;
import com.gestion_tienda_tcg.productos.mapper.CategoriaMapper;
import com.gestion_tienda_tcg.productos.model.Categoria;
import com.gestion_tienda_tcg.productos.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper){
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;


    }
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        log.info("Creando nueva categoría: {}", request.getNombre());

        var categoria = categoriaMapper.toEntity(request);

        var categoriaGuardada = categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(categoriaGuardada);
    }
    public List<CategoriaResponse> listarCategorias() {
        log.info("Listando todas las categorías");
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }


    @Transactional
    public CategoriaResponse editarCategoria(Long id, CategoriaRequest request) {
        log.info("Editando categoría con ID: {}", id);

        // 1. Verificar si existe
        return categoriaRepository.findById(id)
                .map(categoriaExistente -> {

                    categoriaExistente.setNombreTcg(request.getNombre());
                    categoriaExistente.setTipoProducto(request.getTipoProducto());


                    Categoria actualizada = categoriaRepository.save(categoriaExistente);
                    log.info("Categoria ID: {} actualizada exitosamente en la base de datos",id);
                    return categoriaMapper.toResponse(actualizada);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }
    public CategoriaResponse obtenerCategoriaPorID(Long idCategoria) {
        log.info("Obteniendo informacion para categoria con ID: {}",idCategoria);
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow (() -> new CategoriaInvalidaException("No se encontró categoria con ID : " + idCategoria));
        return categoriaMapper.toResponse(categoria);
    }
}




