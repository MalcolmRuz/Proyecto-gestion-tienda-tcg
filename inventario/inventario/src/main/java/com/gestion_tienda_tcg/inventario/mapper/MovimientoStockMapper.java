package com.gestion_tienda_tcg.inventario.mapper;

import com.gestion_tienda_tcg.inventario.dto.MovimientoStockRequest;
import com.gestion_tienda_tcg.inventario.dto.MovimientoStockResponse;
import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import org.springframework.stereotype.Component;

@Component
public class MovimientoStockMapper {
    public MovimientoStock toEntity(MovimientoStockRequest request){

        MovimientoStock movimientoStock = new MovimientoStock();
        movimientoStock.setCantidadMovimiento(request.getCantidadMovimiento());
        movimientoStock.setTipo(request.getTipo());

        return movimientoStock;


    }

    public MovimientoStockResponse toResponse(MovimientoStock movimientoStock){
        return new MovimientoStockResponse(movimientoStock.getIdMovimientoStock(), movimientoStock.getCantidadMovimiento(), movimientoStock.getInventario(),movimientoStock.getTipo(),movimientoStock.getFechaMovimiento());
    }
}
