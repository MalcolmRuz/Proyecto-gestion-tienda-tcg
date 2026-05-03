package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoHistorialService {

    private final CarritoHistorialRepository historialRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoHistorialMapper historialMapper;

    // Registrar historial
    @Transactional
    public void registrarHistorial(
            Carrito carrito,
            EstadoCarrito estado,
            String descripcion) {

        log.info("Registrando historial carrito {}",
                carrito.getIdCarrito());

        CarritoHistorial historial = new CarritoHistorial();

        historial.setCarrito(carrito);
        historial.setEstado(estado);
        historial.setDescripcion(descripcion);

        historialRepository.save(historial);
    }

    // Listar historial carrito
    public List<CarritoHistorialResponse> obtenerHistorial(Long idCarrito) {

        log.info("Obteniendo historial carrito {}",
                idCarrito);

        carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        return historialRepository
                .findByCarritoIdCarritoOrderByFechaDesc(
                        idCarrito)
                .stream()
                .map(historialMapper::toResponse)
                .toList();
    }
}