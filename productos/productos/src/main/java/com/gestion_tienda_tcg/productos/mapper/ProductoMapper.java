package com.gestion_tienda_tcg.productos.mapper;

import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.model.Producto;

public class ProductoMapper {
    public Producto toEntity(ProductoRequest request){
        Producto producto = new Producto();
        producto.setNombreProducto(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setEstadoActivo(request.getEstado());
        return producto;
    }
    public ProductoResponse toResponse(Producto producto){
        return new ProductoResponse(producto.getIdProducto(), producto.getNombreProducto(), producto.getDescripcion(), producto.isEstadoActivo());

    }
}
