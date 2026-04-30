package com.gestion_tienda_tcg.productos.mapper;

import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.model.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class ProveedorMapper {
public Proveedor toEntity(ProveedorRequest request){
    Proveedor proveedor = new Proveedor();
    proveedor.setNombreProveedor(request.getNombre());
    proveedor.setContactoProveedor(request.getContacto());

    return proveedor;



}

public ProveedorResponse toResponse(Proveedor proveedor){
    return new ProveedorResponse(
            proveedor.getIdProveedor(),
            proveedor.getNombreProveedor(),
            proveedor.getContactoProveedor()
    );
}
}
