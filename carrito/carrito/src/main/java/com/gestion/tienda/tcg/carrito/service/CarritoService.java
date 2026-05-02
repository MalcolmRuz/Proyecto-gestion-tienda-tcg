package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
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

    // Crear carrito
    @Transactional
    public CarritoResponse crear(CarritoRequest request) {

        log.info("Creando carrito");

        Carrito carrito = carritoMapper.toEntity(request);

        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        carrito.setTotalCarrito(0.0);

        return carritoMapper.toResponse(carritoRepository.save(carrito));
    }

    // Listar carrito
    public List<CarritoResponse> listar() {

        log.info("Listando carritos");

        return carritoRepository.findAll()
                .stream()
                .map(carritoMapper::toResponse)
                .toList();
    }

    // Buscar carrito por Id
    public CarritoResponse buscarPorId(Long id) {

        log.info("Buscando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        return carritoMapper.toResponse(carrito);
    }

    // 🔹 Actualizar Estado carrito
    @Transactional
    public CarritoResponse actualizarEstado(Long id, EstadoCarrito estado) {

        log.info("Actualizando estado carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carrito.setEstadoCarrito(estado);

        return carritoMapper.toResponse(carrito);
    }

    // Eliminar carrito
    @Transactional
    public void eliminar(Long id) {

        log.warn("Eliminando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carritoRepository.delete(carrito);
    }
}