package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.exception.BadRequestException;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoMapper carritoMapper;
    private final CarritoHistorialService historialService;

    // Crear carrito
    @Transactional
    public CarritoResponse crear(CarritoRequest request) {

        log.info("Creando carrito");

        Carrito carrito = carritoMapper.toEntity(request);

        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        carrito.setTotalCarrito(0.0);

        Carrito guardado = carritoRepository.save(carrito);

        // Registrar historial
        historialService.registrarHistorial(
                guardado,
                guardado.getEstadoCarrito(),
                "Carrito creado");

        return carritoMapper.toResponse(guardado);
    }

    // Listar carritos
    public List<CarritoResponse> listar() {

        log.info("Listando carritos");

        return carritoRepository.findAll()
                .stream()
                .map(carritoMapper::toResponse)
                .toList();
    }

    // Buscar carrito por ID
    public CarritoResponse buscarPorId(Long id) {

        log.info("Buscando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        return carritoMapper.toResponse(carrito);
    }

    // Actualizar estado
    @Transactional
    public CarritoResponse actualizarEstado(
            Long id,
            EstadoCarrito estado) {

        log.info("Actualizando estado del carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        // Validar carrito pagado
        if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO) {

            throw new BadRequestException(
                    "No se puede modificar un carrito pagado");
        }

        carrito.setEstadoCarrito(estado);

        // Registrar historial
        historialService.registrarHistorial(
                carrito,
                estado,
                "Estado actualizado a " + estado);

        return carritoMapper.toResponse(carrito);
    }

    // Eliminar carrito
    @Transactional
    public void eliminar(Long id) {

        log.warn("Eliminando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        if (!carrito.getItems().isEmpty()) {

            throw new BadRequestException(
                    "No se puede eliminar un carrito con items");
        }

        historialService.registrarHistorial(
                carrito,
                carrito.getEstadoCarrito(),
                "Carrito eliminado");

        carritoRepository.delete(carrito);
    }
}