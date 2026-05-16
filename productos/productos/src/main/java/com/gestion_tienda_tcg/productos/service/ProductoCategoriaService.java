package com.gestion_tienda_tcg.productos.service;



import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import com.gestion_tienda_tcg.productos.mapper.ProductoCategoriaMapper;

import com.gestion_tienda_tcg.productos.model.ProductoCategoria;
import com.gestion_tienda_tcg.productos.repository.CategoriaRepository;
import com.gestion_tienda_tcg.productos.repository.ProductoCategoriaRepository;
import com.gestion_tienda_tcg.productos.repository.ProductoRepository;

import jakarta.transaction.Transactional;
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

    public ProductoCategoriaService(ProductoCategoriaRepository productoCategoriaRepository, ProductoCategoriaMapper productoCategoriaMapper,ProductoRepository productoRepository,CategoriaRepository categoriaRepository){
        this.productoCategoriaRepository = productoCategoriaRepository;
        this.productoCategoriaMapper = productoCategoriaMapper;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;

    }

    @Transactional
    public ProductoCategoriaResponse asignarCategoriaAProducto(ProductoCategoriaRequest request) {
        log.info("Asociando producto ID: {} con categoría ID: {}", request.getIdProducto(), request.getIdCategoria());


        var producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        var categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        var nuevaRelacion = new ProductoCategoria();


        nuevaRelacion.setProducto(producto);
        nuevaRelacion.setCategoria(categoria);


        nuevaRelacion.setNombreProducto(producto.getNombreProducto());
        nuevaRelacion.setNombreCategoria(categoria.getNombreTcg());


        var guardado = productoCategoriaRepository.save(nuevaRelacion);


        return productoCategoriaMapper.toResponse(guardado);
    }

    public void quitarCategoriaDeProducto(Long idRelacion) {
        log.info("Eliminando asociación con ID: {}", idRelacion);
        if (!productoCategoriaRepository.existsById(idRelacion)) {
            throw new RuntimeException("La asociación no existe");
        }
        productoCategoriaRepository.deleteById(idRelacion);
    }
    public List<ProductoCategoriaResponse> listarProductosPorCategoria(long idCategoria) {
        log.info("Buscando productos de la categoría ID: {}", idCategoria);
        return productoCategoriaRepository.findByCategoriaIdCategoria(idCategoria)
                .stream()
                .map(productoCategoriaMapper::toResponse)
                .toList();
    }

    public List<ProductoCategoriaResponse> listarCategorias() {
        log.info("Listando todas las asociaciones Producto-Categoría");
        return productoCategoriaRepository.findAll()
                .stream()
                .map(productoCategoriaMapper::toResponse)
                .toList();
    }
}

