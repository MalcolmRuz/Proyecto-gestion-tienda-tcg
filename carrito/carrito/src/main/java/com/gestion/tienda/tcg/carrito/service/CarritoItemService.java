package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.carrito.Client.ProductoClient;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.dto.ProductoDto;
import com.gestion.tienda.tcg.carrito.exception.BadRequestException;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.exception.ItemNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoItemMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;
import com.gestion.tienda.tcg.carrito.repository.CarritoItemRepository;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoItemService {

    private final CarritoItemRepository itemRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoItemMapper itemMapper;
    private final ProductoClient productoClient;

    // Crear items en el carrito
    @Transactional
    public CarritoItemResponse agregarItem(Long idCarrito, CarritoItemRequest request) {

        log.info("Agregando item al carrito {}", idCarrito);

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

        // LLAMADA AL MICROSERVICIO PRODUCTO
        ProductoDto producto;
        try {
            producto = productoClient.obtenerProductoPorId(request.getProductoId());
        } catch (Exception e) {
            log.error("Error al consultar producto: {}", e.getMessage());
            throw new BadRequestException("No se pudo obtener el producto");
        }

        if (producto == null) {
            throw new BadRequestException("Producto no existe");
        }

        // CREAR ITEM
        CarritoItem item = new CarritoItem();
        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(producto.getPrecio()); // 🔥 viene del otro microservicio
        item.setCarrito(carrito);

        double totalItem = producto.getPrecio() * request.getCantidad();
        item.setPrecioTotalItem(totalItem);

        CarritoItem guardado = itemRepository.save(item);

        recalcularTotal(carrito);

        return itemMapper.toResponse(guardado);
    }

    // Listar items por carrito
    public List<CarritoItemResponse> listarPorCarrito(Long idCarrito) {

        log.info("Listando items del carrito {}", idCarrito);

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

        return carrito.getItems()
                .stream()
                .map(itemMapper::toResponse)
                .toList();
    }

    // Obtener item por ID
    public CarritoItemResponse buscarPorId(Long idItem) {

        log.info("Buscando item {}", idItem);

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado"));

        return itemMapper.toResponse(item);
    }

    // Actualizar carrito
    @Transactional
    public CarritoItemResponse actualizarCantidad(Long idItem, Integer cantidad) {

        log.info("Actualizando cantidad del item {}", idItem);

        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        }

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado"));

        item.setCantidad(cantidad);

        double totalItem = item.getPrecioUnitario() * cantidad;
        item.setPrecioTotalItem(totalItem);

        recalcularTotal(item.getCarrito());

        return itemMapper.toResponse(item);
    }

    // Eliminar item del carrito
    @Transactional
    public void eliminarItem(Long idItem) {

        log.warn("Eliminando item {}", idItem);

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado"));

        Carrito carrito = item.getCarrito();

        itemRepository.delete(item);

        recalcularTotal(carrito);
    }

    // Metodo recalcular
    private void recalcularTotal(Carrito carrito) {

        if (carrito.getItems() == null) {
            carrito.setTotalCarrito(0.0);
            return;
        }

        double total = carrito.getItems()
                .stream()
                .mapToDouble(CarritoItem::getPrecioTotalItem)
                .sum();

        carrito.setTotalCarrito(total);
    }
}