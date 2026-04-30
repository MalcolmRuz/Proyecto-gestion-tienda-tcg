package com.gestion_tienda_tcg.inventario.service;

import com.gestion_tienda_tcg.inventario.dto.MovimientoStockRequest;
import com.gestion_tienda_tcg.inventario.dto.MovimientoStockResponse;
import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.exception.InventarioInvalidoException;
import com.gestion_tienda_tcg.inventario.mapper.MovimientoStockMapper;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.model.MovimientoStock;
import com.gestion_tienda_tcg.inventario.repository.InventarioRepository;
import com.gestion_tienda_tcg.inventario.repository.MovimientoStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    public void generarRegistro(){}

    //public MovimientoStockResponse listarHistorialCompleto(){}

    //public MovimientoStockResponse listarMovimientosPorInventario(){}

    public List<MovimientoStockResponse> listarPorTipo(TipoMovimiento tipo) {
        log.info("Consultando movimientos de tipo: {}", tipo);


        List<MovimientoStock> movimientos = movimientoStockRepository.findByTipo(tipo);


        return movimientos.stream()
                .map(movimientoStockMapper::toResponse) // Usando tu mapper
                .toList();
    }

    public List<MovimientoStockResponse> reporteEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Generando reporte de movimientos desde {} hasta {}", inicio, fin);

        if (inicio.isAfter(fin)) {
            throw new InventarioInvalidoException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }


        List<MovimientoStock> movimientos = movimientoStockRepository.findByFechaBetween(inicio, fin);


        return movimientos.stream()
                .map(movimientoStockMapper::toResponse)
                .toList();
    }
}
