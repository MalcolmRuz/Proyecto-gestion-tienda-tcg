package com.gestion.tienda.tcg.carrito.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class CarritoItemServiceTest {

    @Mock
    private CarritoItemRepository itemRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CarritoItemMapper itemMapper;
    @Mock
    private ProductoClient productoClient;
    @Mock
    private InventarioClient inventarioClient;
    @Mock
    private CarritoHistorialService historialService;

    @InjectMocks
    private CarritoItemService itemService;

    // =====================================================================
    // Helpers — usan solo setters (CarritoItemRequest no tiene constructor con
    // args)
    // =====================================================================

    private Carrito carritoActivo(Long id) {
        Carrito c = new Carrito();
        c.setIdCarrito(id);
        c.setEstadoCarrito(EstadoCarrito.ACTIVO);
        return c;
    }

    private CarritoItemRequest request(Long productoId, int cantidad) {
        CarritoItemRequest r = new CarritoItemRequest();
        r.setProductoId(productoId);
        r.setCantidad(cantidad);
        return r;
    }

    // ProductoDto sí tiene @AllArgsConstructor: (Long idProducto, String
    // nombreProducto, String descripcion, Double precioUnitario)
    private ProductoDto producto(Long id, double precio) {
        return new ProductoDto(id, "Producto " + id, "Descripcion", precio);
    }

    // InventarioResponse solo tiene @Data, sin constructor con args
    private InventarioResponse inventario(int stock) {
        InventarioResponse inv = new InventarioResponse();
        inv.setIdInventario(1L);
        inv.setStockActual(stock);
        return inv;
    }

    // CarritoItemResponse tiene @AllArgsConstructor: (Long idItem, Long productoId,
    // String descripcionProducto, Integer cantidad, Double precioUnitario, Double
    // precioTotalItem, Long idCarrito)
    private CarritoItemResponse responseDto(Long idItem, Long productoId, int cantidad, double precioUnit,
            double precioTotal) {
        return new CarritoItemResponse(idItem, productoId, "Descripcion", cantidad, precioUnit, precioTotal, 1L);
    }

    // =====================================================================
    // agregarItem — nuevo item
    // =====================================================================

    @Test
    void agregarItem_deberiaGuardarNuevoItem_cuandoNoExisteEnCarrito() {
        Long idCarrito = 1L;
        Carrito carrito = carritoActivo(idCarrito);

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(producto(1L, 100.0));
        when(inventarioClient.obtenerInventarioPorProducto(1L)).thenReturn(inventario(10));
        when(itemRepository.findByCarrito_IdCarritoAndProductoId(idCarrito, 1L)).thenReturn(Optional.empty());

        CarritoItem itemGuardado = new CarritoItem();
        itemGuardado.setIdItem(1L);
        itemGuardado.setCantidad(2);
        itemGuardado.setPrecioUnitario(100.0);
        itemGuardado.setPrecioTotalItem(200.0);
        itemGuardado.setCarrito(carrito);

        when(itemRepository.save(any(CarritoItem.class))).thenReturn(itemGuardado);
        when(itemRepository.findByCarrito_IdCarrito(idCarrito)).thenReturn(List.of(itemGuardado));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(itemMapper.toResponse(itemGuardado)).thenReturn(responseDto(1L, 1L, 2, 100.0, 200.0));

        CarritoItemResponse result = itemService.agregarItem(idCarrito, request(1L, 2));

        assertNotNull(result);
        assertEquals(200.0, result.getPrecioTotalItem());
        verify(itemRepository).save(any(CarritoItem.class));
        verify(historialService).registrarHistorial(eq(carrito), eq(EstadoCarrito.ACTIVO), anyString());
    }

    @Test
    void agregarItem_deberiaActualizarCantidad_cuandoItemYaExisteEnCarrito() {
        Long idCarrito = 1L;
        Carrito carrito = carritoActivo(idCarrito);

        CarritoItem itemExistente = new CarritoItem();
        itemExistente.setIdItem(1L);
        itemExistente.setProductoId(1L);
        itemExistente.setCantidad(2);
        itemExistente.setPrecioUnitario(50.0);
        itemExistente.setPrecioTotalItem(100.0);
        itemExistente.setCarrito(carrito);

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(producto(1L, 50.0));
        when(inventarioClient.obtenerInventarioPorProducto(1L)).thenReturn(inventario(10));
        when(itemRepository.findByCarrito_IdCarritoAndProductoId(idCarrito, 1L)).thenReturn(Optional.of(itemExistente));
        when(itemRepository.save(itemExistente)).thenReturn(itemExistente);
        when(itemRepository.findByCarrito_IdCarrito(idCarrito)).thenReturn(List.of(itemExistente));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(itemMapper.toResponse(itemExistente)).thenReturn(responseDto(1L, 1L, 5, 50.0, 250.0));

        CarritoItemResponse result = itemService.agregarItem(idCarrito, request(1L, 3));

        assertEquals(5, result.getCantidad());
        assertEquals(250.0, result.getPrecioTotalItem());
    }

    @Test
    void agregarItem_deberiaLanzarExcepcion_siCarritoEstaPagadoOCancelado() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PAGADO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> itemService.agregarItem(1L, request(1L, 1)));

        assertEquals("No se puede agregar ítem a carrito PAGADO o CANCELADO", exception.getMessage());
    }

    @Test
    void agregarItem_deberiaLanzarExcepcion_siStockEsInsuficiente() {
        Carrito carrito = carritoActivo(1L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(producto(1L, 50.0));
        when(inventarioClient.obtenerInventarioPorProducto(1L)).thenReturn(inventario(3));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> itemService.agregarItem(1L, request(1L, 10)));

        assertEquals("Stock insuficiente", exception.getMessage());
    }

    @Test
    void agregarItem_deberiaLanzarExcepcion_siCarritoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        CarritoNotFoundException exception = assertThrows(
                CarritoNotFoundException.class,
                () -> itemService.agregarItem(99L, request(1L, 1)));

        assertEquals("Carrito no encontrado", exception.getMessage());
    }

    // =====================================================================
    // listarPorCarrito
    // =====================================================================

    @Test
    void listarPorCarrito_deberiaRetornarItems_siCarritoExiste() {
        Carrito carrito = carritoActivo(1L);
        CarritoItem item = new CarritoItem();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(itemRepository.findByCarrito_IdCarrito(1L)).thenReturn(List.of(item));
        when(itemMapper.toResponse(item)).thenReturn(new CarritoItemResponse());

        List<CarritoItemResponse> result = itemService.listarPorCarrito(1L);

        assertEquals(1, result.size());
    }

    @Test
    void listarPorCarrito_deberiaLanzarExcepcion_siCarritoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        CarritoNotFoundException exception = assertThrows(
                CarritoNotFoundException.class,
                () -> itemService.listarPorCarrito(99L));

        assertEquals("Carrito no encontrado", exception.getMessage());
    }

    // =====================================================================
    // buscarPorIdEnCarrito
    // =====================================================================

    @Test
    void buscarPorIdEnCarrito_deberiaRetornarItem_siExiste() {
        CarritoItem item = new CarritoItem();

        when(itemRepository.findByCarrito_IdCarritoAndIdItem(1L, 1L)).thenReturn(Optional.of(item));
        when(itemMapper.toResponse(item)).thenReturn(new CarritoItemResponse());

        CarritoItemResponse result = itemService.buscarPorIdEnCarrito(1L, 1L);

        assertNotNull(result);
    }

    @Test
    void buscarPorIdEnCarrito_deberiaLanzarExcepcion_siItemNoExiste() {
        when(itemRepository.findByCarrito_IdCarritoAndIdItem(1L, 99L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemService.buscarPorIdEnCarrito(1L, 99L));

        assertEquals("Item no encontrado", exception.getMessage());
    }

    // =====================================================================
    // actualizarCantidad
    // =====================================================================

    @Test
    void actualizarCantidad_deberiaActualizarYRecalcularTotal() {
        Carrito carrito = carritoActivo(1L);

        CarritoItem item = new CarritoItem();
        item.setIdItem(1L);
        item.setCantidad(2);
        item.setPrecioUnitario(50.0);
        item.setPrecioTotalItem(100.0);
        item.setCarrito(carrito);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemRepository.findByCarrito_IdCarrito(1L)).thenReturn(List.of(item));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(itemMapper.toResponse(item)).thenReturn(new CarritoItemResponse());

        itemService.actualizarCantidad(1L, 5);

        assertEquals(5, item.getCantidad());
        assertEquals(250.0, item.getPrecioTotalItem());
        verify(itemRepository).save(item);
        verify(historialService).registrarHistorial(eq(carrito), any(), anyString());
    }

    @Test
    void actualizarCantidad_deberiaLanzarExcepcion_siItemNoExiste() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemService.actualizarCantidad(99L, 5));

        assertEquals("Item no encontrado", exception.getMessage());
    }

    // =====================================================================
    // eliminarItem
    // =====================================================================

    @Test
    void eliminarItem_deberiaEliminarYRecalcularTotal() {
        Carrito carrito = carritoActivo(1L);

        CarritoItem item = new CarritoItem();
        item.setIdItem(1L);
        item.setCarrito(carrito);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByCarrito_IdCarrito(1L)).thenReturn(List.of());
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        itemService.eliminarItem(1L);

        verify(itemRepository).delete(item);
        verify(historialService).registrarHistorial(eq(carrito), any(), anyString());
    }

    @Test
    void eliminarItem_deberiaLanzarExcepcion_siItemNoExiste() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> itemService.eliminarItem(99L));

        assertEquals("Item no encontrado", exception.getMessage());
    }
}
