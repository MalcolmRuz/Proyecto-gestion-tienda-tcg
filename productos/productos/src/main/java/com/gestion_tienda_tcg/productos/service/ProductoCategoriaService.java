package com.gestion_tienda_tcg.productos.service;



import com.gestion_tienda_tcg.productos.mapper.ProductoCategoriaMapper;

import com.gestion_tienda_tcg.productos.repository.CategoriaRepository;
import com.gestion_tienda_tcg.productos.repository.ProductoCategoriaRepository;
import com.gestion_tienda_tcg.productos.repository.ProductoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductoCategoriaService {
    private final ProductoCategoriaRepository productoCategoriaRepository;
    private final ProductoCategoriaMapper productoCategoriaMapper;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    private ProductoCategoriaService(ProductoCategoriaRepository productoCategoriaRepository, ProductoCategoriaMapper productoCategoriaMapper,ProductoRepository productoRepository,CategoriaRepository categoriaRepository){
        this.productoCategoriaRepository = productoCategoriaRepository;
        this.productoCategoriaMapper = productoCategoriaMapper;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;

    }

    //METODO PARA asignarCategoriaAProducto

    //METODO PARA quitarCategoriaDeProducto
    //public List<ProductoCategoriaResponse> listarProductosPorCategoria(long idCategoria){}

    //public List<ProductoCategoriaResponse> listarCategorias(){}

}
