package com.gestion_tienda_tcg.productos.service;


import com.gestion_tienda_tcg.productos.mapper.CategoriaMapper;
import com.gestion_tienda_tcg.productos.repository.CategoriaRepository;
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

    //public CategoriaResponse crearCategoria(CategoriaRequest request){}
    //public List<CategoriaResponse> listarCategorias(){};
    //public CategoriaResponse editarCategoria(CategoriaRequest request){};



}
