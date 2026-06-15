package com.gestion_tienda_tcg.inventario.service;

import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.exception.InventarioInvalidoException;
import com.gestion_tienda_tcg.inventario.mapper.InventarioMapper;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest{
    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private MovimientoStockService movimientoStockService;
    @Mock
    private InventarioMapper inventarioMapper;
    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void disminuirStock_debeRestarStock_CuandoHayDisponible(){
        Inventario inventarioExistente = new Inventario(1L, 100L,50,null);

        Mockito.when(inventarioRepository.findByIdProducto(100L))
                .thenReturn(Optional.of(inventarioExistente));
        InventarioResponse respuestaFalsa = new InventarioResponse(1L,100L,30,null);
        Mockito.when(inventarioMapper.toResponse(any(Inventario.class))).thenReturn(respuestaFalsa);

        // 2. WHEN
        InventarioResponse resultado = inventarioService.disminuirStock(100L, 20);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(30, inventarioExistente.getStockActual()); // Usando tu stockActual real

        Mockito.verify(inventarioRepository).save(inventarioExistente);
        Mockito.verify(movimientoStockService).generarRegistro(
                inventarioExistente,
                -20,
                TipoMovimiento.SALIDA
        );
    }

    @Test
    void disminuirStock_debeRestarStock_CuandoNoHayDisponible() {

        Inventario inventarioExistente = new Inventario(1L, 100L, 50, null);

        Mockito.when(inventarioRepository.findByIdProducto(100L))
                .thenReturn(Optional.of(inventarioExistente));

        InventarioInvalidoException excepcion = assertThrows(InventarioInvalidoException.class, () -> {
            inventarioService.disminuirStock(100L, 60);
        });

        assertTrue(excepcion.getMessage().contains("Stock insuficiente"));

        assertEquals(50, inventarioExistente.getStockActual());

        Mockito.verify(inventarioRepository, Mockito.never()).save(any(Inventario.class));
        Mockito.verifyNoInteractions(movimientoStockService);
    }
}
