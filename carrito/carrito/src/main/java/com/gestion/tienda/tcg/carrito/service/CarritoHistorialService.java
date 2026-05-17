package com.gestion.tienda.tcg.carrito.service;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoHistorialMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;
import com.gestion.tienda.tcg.carrito.repository.CarritoHistorialRepository;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoHistorialService {

    private final CarritoHistorialRepository historialRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoHistorialMapper historialMapper;

    //=========================
    //Metodo para guardar los carritos creados en el historial
    //=========================

    @Transactional
    public void registrarHistorial(
            Carrito carrito,
            EstadoCarrito estadoCarrito,
            String descripcion){

        log.info("Guardando datos del carrito en el historial {}",
                carrito.getIdCarrito()); //Trae los datos solicitados, para luego almacenarlos en un nuevo historial

        CarritoHistorial historial = new CarritoHistorial();
        historial.setCarrito(carrito);
        historial.setEstado(estadoCarrito);
        historial.setDescripcion(descripcion);

        historialRepository.save(historial);
    }
    //=========================
    //Metodo para Listar carritos guardados en el historial
    //=========================

    public List<CarritoHistorialResponse> obtenerHistorial(Long idCarrito){
        log.info("Obteniendo el historial de carritos {}",
                idCarrito); //A traves del id del carrito, trae la informacion de cada uno para mostrar en historial

        carritoRepository.findById(idCarrito).orElseThrow(()->new CarritoNotFoundException("Carrito no encontrado"));

        return historialRepository
                .findByCarrito_idCarritoOrderByFechaDesc(idCarrito)
                .stream()
                .map(historialMapper::toResponse)
                .toList();
    }
}
