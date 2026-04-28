package com.gestion_tienda_tcg.carrito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion_tienda_tcg.carrito.model.Carrito;
import com.gestion_tienda_tcg.carrito.model.EstadoCarrito;
import com.gestion_tienda_tcg.carrito.repository.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    // Metodo para listar los carritos

    public List<Carrito> obtenerCarritos() {
        return carritoRepository.findAll();
    }

    // Metodo para crear un carrito
    public Carrito crearCarrito(Carrito carrito) {

        // El carrito si no viene con su respectivo estado, se deja ACTIVO por defecto
        if (carrito.getEstadoCarrito() == null) {
            carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        }

        // Se inicia el carrito en $ 0
        carrito.setTotalCarrito(0.0);

        return carritoRepository.save(carrito);
    }

    // Metodo para buscar un carrito por ID

    public Carrito obtenerCarritoPorId(Long id) {
        return carritoRepository.findById(id)
                .orElse(null);
    }

    // Metodo para actualizar un carrito

    // Corregir

    /*
     * public Carrito actualizarCarrito(Long id, Carrito carritoActualizado) {
     * Carrito carrito = carritoRepository.findById(id).orElse(null);
     * if (carrito = null)
     * return null;
     * 
     * carrito.setEstadoCarrito(carritoActualizado.getEstadoCarrito());
     * 
     * return carritoRepository.save(carrito);
     * }
     */

    // Metodo para eliminar Carrito

    public void eliminarCarrito(Long id) {
        carritoRepository.deleteById(id);
    }

}
