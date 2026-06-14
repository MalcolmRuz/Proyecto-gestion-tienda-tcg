package com.gestion.tienda.tcg.carrito.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.tienda.tcg.carrito.dto.CarritoHistorialResponse;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoHistorialMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoHistorial;
import com.gestion.tienda.tcg.carrito.repository.CarritoHistorialRepository;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;

@ExtendWith(MockitoExtension.class)
class CarritoHistorialServiceTest {

    @Mock
    private CarritoHistorialRepository historialRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CarritoHistorialMapper historialMapper;

    @InjectMocks
    private CarritoHistorialService historialService;

    // =====================================================================
    // registrarHistorial
    // =====================================================================

    @Test
    void registrarHistorial_deberiaGuardarEntidadEnRepositorio() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);

        historialService.registrarHistorial(carrito, EstadoCarrito.ACTIVO, "Carrito creado");

        verify(historialRepository).save(any(CarritoHistorial.class));
    }

    @Test
    void registrarHistorial_deberiaGuardarConEstadoCorrecto() {
        Carrito carrito = new Carrito();
        carrito.setIdCarrito(1L);

        historialService.registrarHistorial(carrito, EstadoCarrito.PAGADO, "Pago confirmado");

        verify(historialRepository).save(any(CarritoHistorial.class));
    }

    // =====================================================================
    // obtenerHistorial
    // =====================================================================

    @Test
    void obtenerHistorial_deberiaRetornarListaOrdenada_siCarritoExiste() {
        Long idCarrito = 1L;
        CarritoHistorial h1 = new CarritoHistorial();
        CarritoHistorial h2 = new CarritoHistorial();

        when(carritoRepository.findById(idCarrito)).thenReturn(Optional.of(new Carrito()));
        when(historialRepository.findByCarrito_idCarritoOrderByFechaDesc(idCarrito))
                .thenReturn(List.of(h1, h2));
        when(historialMapper.toResponse(any(CarritoHistorial.class)))
                .thenReturn(new CarritoHistorialResponse());

        List<CarritoHistorialResponse> result = historialService.obtenerHistorial(idCarrito);

        assertFalse(result.isEmpty());
        verify(carritoRepository).findById(idCarrito);
        verify(historialRepository).findByCarrito_idCarritoOrderByFechaDesc(idCarrito);
    }

    @Test
    void obtenerHistorial_deberiaLanzarExcepcion_siCarritoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        CarritoNotFoundException exception = assertThrows(
                CarritoNotFoundException.class,
                () -> historialService.obtenerHistorial(99L));

        assertEquals("Carrito no encontrado", exception.getMessage());
    }
}
