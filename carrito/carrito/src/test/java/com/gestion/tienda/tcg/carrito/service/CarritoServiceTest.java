package com.gestion.tienda.tcg.carrito.service;

import java.util.Collections;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.tienda.tcg.carrito.Client.InventarioClient;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.dto.InventarioResponse;
import com.gestion.tienda.tcg.carrito.dto.PagarCarritoRequest;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.exception.BadRequestException;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CarritoMapper carritoMapper;
    @Mock
    private CarritoHistorialService historialService;
    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private CarritoService carritoService;

    // =====================================================================
    // Helpers
    // =====================================================================

    // PagarCarritoRequest solo tiene @Data: usar setters
    private PagarCarritoRequest pagarRequest(Long usuarioId) {
        PagarCarritoRequest r = new PagarCarritoRequest();
        r.setUsuarioId(usuarioId);
        return r;
    }

    // InventarioResponse solo tiene @Data: usar setters
    private InventarioResponse inventario(int stock) {
        InventarioResponse inv = new InventarioResponse();
        inv.setStockActual(stock);
        return inv;
    }

    private CarritoItem item(Long productoId, int cantidad) {
        CarritoItem i = new CarritoItem();
        i.setProductoId(productoId);
        i.setCantidad(cantidad);
        return i;
    }

    // =====================================================================
    // crearCarrito
    // =====================================================================

    @Test
    void crearCarrito_deberiaGuardarYRegistrarHistorial() {
        CarritoRequest request = new CarritoRequest();
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);

        when(carritoMapper.toEntity(request)).thenReturn(carrito);
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        CarritoResponse response = new CarritoResponse();
        response.setIdCarrito(1L);
        response.setEstadoCarrito(EstadoCarrito.ACTIVO);
        response.setTotalCarrito(0.0);
        when(carritoMapper.toResponse(carrito)).thenReturn(response);

        CarritoResponse result = carritoService.crearCarrito(request);

        assertNotNull(result);
        assertEquals(EstadoCarrito.ACTIVO, result.getEstadoCarrito());
        verify(carritoRepository).save(carrito);
        verify(historialService).registrarHistorial(carrito, EstadoCarrito.ACTIVO, "Carrito creado");
    }

    // =====================================================================
    // buscarPorId
    // =====================================================================

    @Test
    void buscarPorId_deberiaRetornarCarrito_siExiste() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        CarritoResponse response = new CarritoResponse();
        response.setIdCarrito(1L);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoMapper.toResponse(carrito)).thenReturn(response);

        CarritoResponse result = carritoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdCarrito());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcion_siNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        CarritoNotFoundException exception = assertThrows(
                CarritoNotFoundException.class,
                () -> carritoService.buscarPorId(99L));

        assertEquals("Carrito no encontrado", exception.getMessage());
    }

    // =====================================================================
    // listar
    // =====================================================================

    @Test
    void listar_deberiaRetornarListaDeCarritos() {
        Carrito carrito = new Carrito();
        CarritoResponse response = new CarritoResponse();

        when(carritoRepository.findAll()).thenReturn(List.of(carrito));
        when(carritoMapper.toResponse(carrito)).thenReturn(response);

        List<CarritoResponse> result = carritoService.listar();

        assertEquals(1, result.size());
    }

    // =====================================================================
    // cancelarCarrito
    // =====================================================================

    @Test
    void cancelarCarrito_deberiaLanzarExcepcion_siYaEstaPagado() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PAGADO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> carritoService.cancelarCarrito(1L));

        assertEquals("No se puede cancelar un carrito pagado", ex.getMessage());
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void cancelarCarrito_deberiaCancelarCarritoActivo() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.cancelarCarrito(1L);

        assertEquals(EstadoCarrito.CANCELADO, carrito.getEstadoCarrito());
        verify(historialService).registrarHistorial(carrito, EstadoCarrito.CANCELADO, "Carrito cancelado");
    }

    // =====================================================================
    // reactivarCarrito
    // =====================================================================

    @Test
    void reactivarCarrito_deberiaLanzarExcepcion_siNoEstaPendiente() {
        Carrito carrito = new Carrito();
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.reactivarCarrito(1L));

        assertEquals("No se puede reactivar un carrito que ya está activo", exception.getMessage());
    }

    @Test
    void reactivarCarrito_deberiaReactivar_siEstaPendiente() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.reactivarCarrito(1L);

        assertEquals(EstadoCarrito.ACTIVO, carrito.getEstadoCarrito());
        verify(historialService).registrarHistorial(carrito, EstadoCarrito.ACTIVO, "Carrito reactivado");
    }

    // =====================================================================
    // eliminar
    // =====================================================================

    @Test
    void eliminar_deberiaLanzarExcepcion_siCarritoEstaPagado() {
        Carrito carrito = new Carrito();
        carrito.setEstadoCarrito(EstadoCarrito.PAGADO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.eliminar(1L));

        assertEquals("No se puede eliminar un carrito PAGADO", exception.getMessage());
        verify(carritoRepository, never()).delete(any());
    }

    @Test
    void eliminar_deberiaEliminarCarritoActivo() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        carritoService.eliminar(1L);

        verify(carritoRepository).delete(carrito);
        verify(historialService).registrarHistorial(carrito, EstadoCarrito.CANCELADO, "Carrito eliminado físicamente");
    }

    // =====================================================================
    // pagarCarrito
    // =====================================================================

    @Test
    void pagarCarrito_deberiaLanzarExcepcion_siEstadoNoEsActivoNiPendiente() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.CANCELADO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.pagarCarrito(1L, pagarRequest(1L)));

        assertEquals("No se puede pagar un carrito CANCELADO", exception.getMessage());
    }

    @Test
    void pagarCarrito_deberiaLanzarExcepcion_siCarritoEstaVacio() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        carrito.setItems(Collections.emptyList());

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.pagarCarrito(1L, pagarRequest(1L)));

        assertEquals("No se puede pagar un carrito vacío", exception.getMessage());
    }

    @Test
    void pagarCarrito_deberiaMarcarsePendiente_siHayStockInsuficiente() {
        CarritoItem i = item(1L, 5);
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        carrito.setItems(List.of(i));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(inventarioClient.obtenerInventarioPorProducto(1L)).thenReturn(inventario(2));
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.pagarCarrito(1L, pagarRequest(1L)));

        assertEquals("Stock insuficiente", exception.getMessage());
        assertEquals(EstadoCarrito.PENDIENTE, carrito.getEstadoCarrito());
    }

    @Test
    void pagarCarrito_deberiaPasarAPendienteValidacion_siStockEsSuficiente() {
        CarritoItem i = item(1L, 2);
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
        carrito.setItems(List.of(i));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(inventarioClient.obtenerInventarioPorProducto(1L)).thenReturn(inventario(10));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.pagarCarrito(1L, pagarRequest(1L));

        assertEquals(EstadoCarrito.PENDIENTE_VALIDACION, carrito.getEstadoCarrito());
        verify(historialService).registrarHistorial(
                eq(carrito), eq(EstadoCarrito.PENDIENTE_VALIDACION), anyString());
    }

    // =====================================================================
    // confirmarPago
    // =====================================================================

    @Test
    void confirmarPago_deberiaLanzarExcepcion_siNoEstaPendienteValidacion() {
        Carrito carrito = new Carrito();
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.confirmarPago(1L, 1L));

        assertEquals("El carrito no está pendiente de validación", exception.getMessage());
    }

    @Test
    void confirmarPago_deberiaPagarCarritoYDescontarStock() {
        CarritoItem i = item(1L, 3);
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE_VALIDACION);
        carrito.setItems(List.of(i));

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.confirmarPago(1L, 1L);

        assertEquals(EstadoCarrito.PAGADO, carrito.getEstadoCarrito());
        // disminuirStock recibe Integer, no int
        verify(inventarioClient).disminuirStock(1L, 3);
        verify(historialService).registrarHistorial(carrito, EstadoCarrito.PAGADO, "Pago confirmado");
    }

    // =====================================================================
    // rechazarPago
    // =====================================================================

    @Test
    void rechazarPago_deberiaLanzarExcepcion_siNoEstaPendienteValidacion() {
        Carrito carrito = new Carrito();
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.rechazarPago(1L, false));

        assertEquals("No se puede rechazar pago si el carrito no está en PENDIENTE_VALIDACION",
                exception.getMessage());
    }

    @Test
    void rechazarPago_deberiaMarCarCancelado_siCancelarEsTrue() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE_VALIDACION);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.rechazarPago(1L, true);

        assertEquals(EstadoCarrito.CANCELADO, carrito.getEstadoCarrito());
    }

    @Test
    void rechazarPago_deberiaVolverAPendiente_siCancelarEsFalse() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE_VALIDACION);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoMapper.toResponse(carrito)).thenReturn(new CarritoResponse());

        carritoService.rechazarPago(1L, false);

        assertEquals(EstadoCarrito.PENDIENTE, carrito.getEstadoCarrito());
    }

    // =====================================================================
    // actualizarEstadoExterno
    // =====================================================================

    @Test
    void actualizarEstadoExterno_deberiaLanzarExcepcion_siEstadoEsInvalido() {
        Carrito carrito = new Carrito();
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> carritoService.actualizarEstadoExterno(1L, "ESTADO_INEXISTENTE"));

        assertEquals("Estado inválido", exception.getMessage());
    }

    @Test
    void actualizarEstadoExterno_deberiaActualizarEstado_siEsValido() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);

        carritoService.actualizarEstadoExterno(1L, "PAGADO");

        assertEquals(EstadoCarrito.PAGADO, carrito.getEstadoCarrito());
    }
}
