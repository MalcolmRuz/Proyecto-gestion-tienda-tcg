package com.gestion_tienda_tcg.productos.mapper;

import com.gestion_tienda_tcg.productos.dto.CategoriaRequest;
import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRequest request){
        Categoria categoria = new  Categoria();

        categoria.setNombreTcg(request.getNombre());
        categoria.setTipoProducto(request.getTipoProducto());

        return categoria;
    }

    public CategoriaResponse toResponse(Categoria categoria){
        return new CategoriaResponse(
                categoria.getIdCategoria(),
                categoria.getNombreTcg(),
                categoria.getTipoProducto());
    }
}
