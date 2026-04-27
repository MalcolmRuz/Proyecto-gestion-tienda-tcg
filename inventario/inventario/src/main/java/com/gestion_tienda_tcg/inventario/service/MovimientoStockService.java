package com.gestion_tienda_tcg.inventario.service;

import com.gestion_tienda_tcg.inventario.dto.MovimientoStockRequest;
import com.gestion_tienda_tcg.inventario.dto.MovimientoStockResponse;
import com.gestion_tienda_tcg.inventario.mapper.MovimientoStockMapper;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import com.gestion_tienda_tcg.inventario.repository.InventarioRepository;
import com.gestion_tienda_tcg.inventario.repository.MovimientoStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MovimientoStockService {
    private final MovimientoStockMapper movimientoStockMapper;
    private final MovimientoStockRepository movimientoStockRepository;
    private final InventarioRepository inventarioRepository;
    public MovimientoStockService(MovimientoStockMapper movimientoStockMapper, MovimientoStockRepository movimientoStockRepository,InventarioRepository inventarioRepository) {

        this.movimientoStockMapper = movimientoStockMapper;
        this.movimientoStockRepository = movimientoStockRepository;
        this.inventarioRepository =  inventarioRepository;
    }
}
