package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.gestion.tienda.tcg.carrito.Client.InventarioClient;
import com.gestion.tienda.tcg.carrito.Client.ProductoClient;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.carrito.dto.InventarioResponse;
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
        private final InventarioClient inventarioClient;
        private final CarritoHistorialService historialService;

        // =========================
        // Agregar item al carrito
        // =========================
        @Transactional
        public CarritoItemResponse agregarItem(@NonNull Long idCarrito, CarritoItemRequest request) {

                Carrito carrito = carritoRepository.findById(idCarrito)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO
                                || carrito.getEstadoCarrito() == EstadoCarrito.CANCELADO) {
                        throw new BadRequestException("No se puede modificar este carrito");
                }

                ProductoDto producto;
                try {
                        producto = productoClient.obtenerProductoPorId(request.getProductoId());
                } catch (Exception e) {
                        throw new BadRequestException("Error consultando producto");
                }

                if (producto == null || producto.getPrecioUnitario() == null) {
                        throw new BadRequestException("Producto inválido");
                }

                InventarioResponse inventario;
                try {
                        inventario = inventarioClient.obtenerInventarioPorProducto(request.getProductoId());
                } catch (Exception e) {
                        throw new BadRequestException("Error consultando inventario");
                }

                if (inventario.getStockActual() < request.getCantidad()) {
                        throw new BadRequestException("Stock insuficiente");
                }

                CarritoItem itemExistente = itemRepository
                                .findByCarrito_IdCarritoAndProductoId(idCarrito, request.getProductoId())
                                .orElse(null);

                // SUMAR ITEM EXISTENTE
                if (itemExistente != null) {
                        int nuevaCantidad = itemExistente.getCantidad() + request.getCantidad();

                        if (inventario.getStockActual() < nuevaCantidad) {
                                throw new BadRequestException("Stock insuficiente");
                        }

                        itemExistente.setCantidad(nuevaCantidad);
                        itemExistente.setPrecioTotalItem(itemExistente.getPrecioUnitario() * nuevaCantidad);

                        CarritoItem actualizado = itemRepository.save(itemExistente);

                        recalcularTotal(carrito);

                        historialService.registrarHistorial(carrito, carrito.getEstadoCarrito(),
                                        "Cantidad actualizada");

                        return itemMapper.toResponse(actualizado);
                }

                // NUEVO ITEM
                CarritoItem item = new CarritoItem();
                item.setProductoId(request.getProductoId());
                item.setCantidad(request.getCantidad());
                item.setPrecioUnitario(producto.getPrecioUnitario());
                item.setDescripcionProducto(producto.getDescripcion());
                item.setCarrito(carrito);

                item.setPrecioTotalItem(producto.getPrecioUnitario() * request.getCantidad());

                CarritoItem guardado = itemRepository.save(item);

                recalcularTotal(carrito);

                historialService.registrarHistorial(carrito, carrito.getEstadoCarrito(), "Producto agregado");

                return itemMapper.toResponse(guardado);
        }

        // =========================
        // Listar items de un carrito
        // =========================
        public List<CarritoItemResponse> listarPorCarrito(@NonNull Long idCarrito) {
                carritoRepository.findById(idCarrito)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                return itemRepository.findByCarrito_IdCarrito(idCarrito)
                                .stream()
                                .map(itemMapper::toResponse)
                                .toList();
        }

        // =========================
        // Buscar item específico dentro de un carrito
        // =========================
        public CarritoItemResponse buscarPorIdEnCarrito(@NonNull Long idCarrito, @NonNull Long idItem) {
                CarritoItem item = itemRepository.findByCarrito_IdCarritoAndIdItem(idCarrito, idItem)
                                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado en este carrito"));

                return itemMapper.toResponse(item);
        }

        // =========================
        // Actualizar cantidad de un item
        // =========================
        @Transactional
        public CarritoItemResponse actualizarCantidad(@NonNull Long idItem, Integer cantidad) {
                CarritoItem item = itemRepository.findById(idItem)
                                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado"));

                item.setCantidad(cantidad);
                item.setPrecioTotalItem(item.getPrecioUnitario() * cantidad);

                // FIX: se persistía el cambio en memoria pero nunca se guardaba en BD
                CarritoItem actualizado = itemRepository.save(item);

                recalcularTotal(actualizado.getCarrito());

                historialService.registrarHistorial(actualizado.getCarrito(),
                                actualizado.getCarrito().getEstadoCarrito(),
                                "Cantidad actualizada");

                return itemMapper.toResponse(actualizado);
        }

        // =========================
        // Eliminar item del carrito
        // =========================
        @Transactional
        public void eliminarItem(@NonNull Long idItem) {
                CarritoItem item = itemRepository.findById(idItem)
                                .orElseThrow(() -> new ItemNotFoundException("Item no encontrado"));

                Carrito carrito = item.getCarrito();

                itemRepository.delete(item);

                recalcularTotal(carrito);

                historialService.registrarHistorial(carrito, carrito.getEstadoCarrito(), "Producto eliminado");
        }

        // =========================
        // Recalcular total del carrito
        // =========================
        private void recalcularTotal(Carrito carrito) {
                List<CarritoItem> items = itemRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());

                double total = items.stream()
                                .mapToDouble(CarritoItem::getPrecioTotalItem)
                                .sum();

                carrito.setTotalCarrito(total);
                carritoRepository.save(carrito);
        }
}