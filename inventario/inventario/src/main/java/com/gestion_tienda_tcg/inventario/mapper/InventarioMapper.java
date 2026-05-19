package com.gestion_tienda_tcg.inventario.mapper;

import com.gestion_tienda_tcg.inventario.dto.InventarioDetalleResponse;
import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {
    public Inventario toEntity(InventarioRequest request){

        Inventario inventario = new Inventario();
        inventario.setStockActual(request.getStockActual());
        inventario.setIdProducto(request.getIdProducto());
        return inventario;

    }
    public InventarioResponse toResponse(Inventario inventario){
        return new InventarioResponse(
                inventario.getIdInventario(),
                inventario.getIdProducto(),
                inventario.getStockActual(),
                inventario.getFechaInventario());
    }
    public InventarioDetalleResponse toDetalleResponse(Inventario inventario, String nombreProducto) {
        return new InventarioDetalleResponse(
                inventario.getIdInventario(),
                inventario.getIdProducto(),
                nombreProducto,
                inventario.getStockActual(),
                inventario.getFechaInventario()
        );
    }
}


