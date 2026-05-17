package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.carrito.Client.ProductoClient;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.dto.ProductoDto;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
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
    private final CarritoHistorialService historialService;

    // Agregar item al carrito
    @Transactional
    public CarritoItemResponse agregarItem(
            Long idCarrito,
            CarritoItemRequest request) {

        log.info("Agregando item al carrito {}", idCarrito);

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        // Validar estado carrito
        if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO) {

            throw new BadRequestException(
                    "No se puede modificar un carrito pagado");
        }

        // Validar item duplicado
        boolean existe = itemRepository
                .findByCarrito_IdCarritoAndProductoId(
                        idCarrito,
                        request.getProductoId())
                .isPresent();

        if (existe) {

            throw new BadRequestException(
                    "El producto ya existe en el carrito");
        }

        // Consultar microservicio producto
        ProductoDto producto;

        try {

            producto = productoClient
                    .obtenerProductoPorId(
                            request.getProductoId());

        } catch (Exception e) {

            log.error(
                    "Error al consultar producto: {}",
                    e.getMessage());

            throw new BadRequestException(
                    "No se pudo obtener el producto");
        }

        if (producto == null) {

            throw new BadRequestException(
                    "Producto no existe");
        }

        // Crear item
        CarritoItem item = new CarritoItem();

        item.setProductoId(request.getProductoId());
        item.setCantidad(request.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        item.setCarrito(carrito);

        double totalItem = producto.getPrecio() * request.getCantidad();

        item.setPrecioTotalItem(totalItem);

        CarritoItem guardado = itemRepository.save(item);

        recalcularTotal(carrito);

        // Registrar historial
        historialService.registrarHistorial(
                carrito,
                carrito.getEstadoCarrito(),
                "Producto agregado al carrito");

        return itemMapper.toResponse(guardado);
    }

    // Listar items del carrito
    public List<CarritoItemResponse> listarPorCarrito(
            Long idCarrito) {

        log.info("Listando items del carrito {}", idCarrito);

        carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        return itemRepository.findByCarrito_IdCarrito(idCarrito)
                .stream()
                .map(itemMapper::toResponse)
                .toList();
    }

    // Buscar item por ID
    public CarritoItemResponse buscarPorId(Long idItem) {

        log.info("Buscando item {}", idItem);

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item no encontrado"));

        return itemMapper.toResponse(item);
    }

    // Actualizar cantidad
    @Transactional
    public CarritoItemResponse actualizarCantidad(
            Long idItem,
            Integer cantidad) {

        log.info("Actualizando cantidad del item {}", idItem);

        if (cantidad <= 0) {

            throw new BadRequestException(
                    "La cantidad debe ser mayor a 0");
        }

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item no encontrado"));

        // Validar carrito pagado
        if (item.getCarrito().getEstadoCarrito() == EstadoCarrito.PAGADO) {

            throw new BadRequestException(
                    "No se puede modificar un carrito pagado");
        }

        item.setCantidad(cantidad);

        double totalItem = item.getPrecioUnitario() * cantidad;

        item.setPrecioTotalItem(totalItem);

        recalcularTotal(item.getCarrito());

        // Registrar historial
        historialService.registrarHistorial(
                item.getCarrito(),
                item.getCarrito().getEstadoCarrito(),
                "Cantidad actualizada del producto");

        return itemMapper.toResponse(item);
    }

    // Eliminar item
    @Transactional
    public void eliminarItem(Long idItem) {

        log.warn("Eliminando item {}", idItem);

        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item no encontrado"));

        // Validar carrito pagado
        if (item.getCarrito().getEstadoCarrito() == EstadoCarrito.PAGADO) {

            throw new BadRequestException(
                    "No se puede modificar un carrito pagado");
        }

        Carrito carrito = item.getCarrito();

        itemRepository.delete(item);

        recalcularTotal(carrito);

        // Registrar historial
        historialService.registrarHistorial(
                carrito,
                carrito.getEstadoCarrito(),
                "Producto eliminado del carrito");
    }

    // Recalcular total carrito
    private void recalcularTotal(Carrito carrito) {

        List<CarritoItem> items = itemRepository.findByCarrito_IdCarrito(
                carrito.getIdCarrito());

        if (items.isEmpty()) {

            carrito.setTotalCarrito(0.0);

            carritoRepository.save(carrito);

            return;
        }

        double total = items.stream()
                .mapToDouble(CarritoItem::getPrecioTotalItem)
                .sum();

        carrito.setTotalCarrito(total);

        carritoRepository.save(carrito);
    }
}
