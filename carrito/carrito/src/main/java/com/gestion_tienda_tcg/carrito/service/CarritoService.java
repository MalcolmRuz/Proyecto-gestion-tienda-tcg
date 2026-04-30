package com.gestion_tienda_tcg.carrito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion_tienda_tcg.carrito.enums.EstadoCarrito;
import com.gestion_tienda_tcg.carrito.model.Carrito;
import com.gestion_tienda_tcg.carrito.repository.CarritoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    // Listar
    public List<Carrito> listar() {
        log.info("Listando carritos");
        return carritoRepository.findAll();
    }

    // Buscar por Id
    public Carrito buscarPorId(Long idCarrito) {
        log.info("Buscando carrito {}", idCarrito);

        return carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    // Nuevo carrito
    public Carrito crear(Carrito carrito) {
        log.info("Creando carrito");
        return carritoRepository.save(carrito);
    }

    // Actualizar Estado carrito
    public Carrito actualizarEstado(Long idCarrito, EstadoCarrito estado) {
        log.info("Actualizando estado carrito {}", idCarrito);

        Carrito carrito = buscarPorId(idCarrito);
        carrito.setEstadoCarrito(estado);

        return carritoRepository.save(carrito);
    }

    // Eliminar carrito
    public void eliminar(Long idCarrito) {
        log.warn("Eliminando carrito {}", idCarrito);
        carritoRepository.deleteById(idCarrito);
    }

}
