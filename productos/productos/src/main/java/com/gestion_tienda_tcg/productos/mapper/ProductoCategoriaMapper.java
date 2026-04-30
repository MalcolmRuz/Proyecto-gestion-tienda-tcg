package com.gestion_tienda_tcg.productos.mapper;

import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaRequest;

import com.gestion_tienda_tcg.productos.dto.ProductoCategoriaResponse;
import com.gestion_tienda_tcg.productos.model.ProductoCategoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoCategoriaMapper {
    public ProductoCategoria toEntity(ProductoCategoriaRequest request){
        ProductoCategoria productoCategoria = new ProductoCategoria();

        productoCategoria.setNombreProducto(request.getNombreProducto());
        productoCategoria.setNombreCategoria(request.getNombreCategoria());
        return productoCategoria;


    }
    public ProductoCategoriaResponse toResponse(ProductoCategoria entity){
        return new ProductoCategoriaResponse(
                entity.getIdProductoCategoria(),
                entity.getProducto().getIdProducto(),
                entity.getNombreProducto(),
                entity.getCategoria().getIdCategoria(),
                entity.getNombreCategoria(),
                entity.getFechaAsignacion());
    }

}
