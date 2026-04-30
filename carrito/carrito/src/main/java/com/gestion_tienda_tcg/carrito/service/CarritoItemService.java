package com.gestion_tienda_tcg.carrito.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion_tienda_tcg.carrito.model.Carrito;
import com.gestion_tienda_tcg.carrito.model.CarritoItem;
import com.gestion_tienda_tcg.carrito.repository.CarritoItemRepository;
import com.gestion_tienda_tcg.carrito.repository.CarritoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarritoItemService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    // Agregar item

    public CarritoItem agregarItem(Long idCarrito, CarritoItem item) {

        log.info("Agregando item al carrito {}", idCarrito);

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Asociar carrito
        item.setCarrito(carrito);

        // Calcular total del item
        double totalItem = item.getPrecioUnitario() * item.getCantidad();
        item.setPrecioTotalItem(totalItem);

        CarritoItem guardado = carritoItemRepository.save(item);

        recalcularTotal(carrito.getIdCarrito());

        return guardado;
    }

    // Listar items

    public List<CarritoItem> listarItems(Long idCarrito) {

        log.info("Listando items del carrito {}", idCarrito);

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        return carrito.getItems();
    }

    // Actualizar Item

    public CarritoItem actualizarCantidad(Long idItem, Integer cantidad) {

        log.info("Actualizando item {}", idItem);

        CarritoItem item = carritoItemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        item.setCantidad(cantidad);

        double totalItem = item.getPrecioUnitario() * cantidad;
        item.setPrecioTotalItem(totalItem);

        CarritoItem actualizado = carritoItemRepository.save(item);

        recalcularTotal(item.getCarrito().getIdCarrito());

        return actualizado;
    }

    // Eliminar Item

    public void eliminarItem(Long idItem) {

        log.warn("Eliminando item {}", idItem);

        CarritoItem item = carritoItemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        Long idCarrito = item.getCarrito().getIdCarrito();

        carritoItemRepository.delete(item);

        recalcularTotal(idCarrito);
    }

    // Calcular total

    private void recalcularTotal(Long idCarrito) {

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        double total = 0.0;

        if (carrito.getItems() != null) {
            for (CarritoItem item : carrito.getItems()) {
                total += item.getPrecioTotalItem();
            }
        }

        carrito.setTotalCarrito(total);

        carritoRepository.save(carrito);
    }
}
