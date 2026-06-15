package com.gestion_tienda_tcg.productos.service;


import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import com.gestion_tienda_tcg.productos.client.InventarioClient;

import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.exception.ProductoInvalidoException;
import com.gestion_tienda_tcg.productos.mapper.ProductoMapper;
import com.gestion_tienda_tcg.productos.model.Producto;
import com.gestion_tienda_tcg.productos.model.Proveedor;
import com.gestion_tienda_tcg.productos.repository.ProductoRepository;
import com.gestion_tienda_tcg.productos.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private ProductoMapper productoMapper;
    @Mock
    private InventarioClient inventarioClient;
    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProductoService productoService;


    // TEST 1: Flujo exitoso — producto guardado e inventario inicializado

    @Test
    void guardarProducto_debeRetornarResponse_CuandoTodoEsCorrecto() {
        // 1. GIVEN
        ProductoRequest request = new ProductoRequest(
                "Pikachu V",         // nombreProducto
                "Carta Pokémon TCG", // descripcion
                true,                // estado
                1L,                  // idProveedor
                30000D               // precioUnitario
        );

        Proveedor proveedorFalso = new Proveedor();
        proveedorFalso.setIdProveedor(1L);

        Producto productoEntidad = new Producto();
        productoEntidad.setNombreProducto("Pikachu V");

        Producto productoGuardado = new Producto();
        productoGuardado.setIdProducto(10L);
        productoGuardado.setNombreProducto("Pikachu V");

        ProductoResponse respuestaEsperada = new ProductoResponse(10L, "Pikachu V", "Carta Pokémon TCG", true, 1L, 30000D);

        Mockito.when(productoMapper.toEntity(request)).thenReturn(productoEntidad);
        Mockito.when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedorFalso));
        Mockito.when(productoRepository.saveAndFlush(productoEntidad)).thenReturn(productoGuardado);
        Mockito.when(productoMapper.toResponse(productoGuardado)).thenReturn(respuestaEsperada);

        // 2. WHEN
        ProductoResponse resultado = productoService.guardarProducto(request);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdProducto());
        assertEquals("Pikachu V", resultado.getNombreProducto());

        // Verifica que el inventario fue inicializado con stockActual = 0
        Mockito.verify(inventarioClient).inicializarInventario(
                Mockito.argThat(inv ->
                        inv.getIdProducto().equals(10L) && inv.getStockActual() == 0
                )
        );
        Mockito.verify(productoRepository).saveAndFlush(productoEntidad);
    }


    // TEST 2: Proveedor inexistente — debe lanzar excepción y NO guardar

    @Test
    void guardarProducto_debeLanzarExcepcion_CuandoProveedorNoExiste() {
        // 1. GIVEN
        ProductoRequest request = new ProductoRequest(
                "Charizard Ex",         // nombreProducto
                "Carta Pokémon TCG", // descripcion
                true,                // estado
                99L,                  // idProveedor
                30000D               // precioUnitario
        );  // proveedor que no existe

        Producto productoEntidad = new Producto();
        productoEntidad.setNombreProducto("Charizard EX");

        Mockito.when(productoMapper.toEntity(request)).thenReturn(productoEntidad);
        Mockito.when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        // 2. WHEN + 3. THEN
        ProductoInvalidoException excepcion = assertThrows(ProductoInvalidoException.class, () -> {
            productoService.guardarProducto(request);
        });

        assertTrue(excepcion.getMessage().contains("proveedor no existe"));

        // El producto NUNCA debe guardarse si el proveedor no existe
        Mockito.verify(productoRepository, Mockito.never()).saveAndFlush(any(Producto.class));

        // El inventario NUNCA debe inicializarse si no se guardó el producto
        Mockito.verifyNoInteractions(inventarioClient);
    }

    @Test
    void modificarProducto_debeRetornarResponseActualizado_CuandoProductoYProveedorExisten() {
        // 1. GIVEN
        Long idProducto = 5L;
        Long idProveedor = 2L;  // ← un solo ID, usado en ambos sitios

        ProductoRequest request = new ProductoRequest(
                "Mewtwo GX",
                "Carta rara holográfica",
                true,
                idProveedor,   // ← antes tenías 99L aquí, discrepancia con el stub
                30000D
        );

        Proveedor proveedorFalso = new Proveedor();
        proveedorFalso.setIdProveedor(idProveedor);

        Producto productoExistente = new Producto();
        productoExistente.setIdProducto(idProducto);
        productoExistente.setNombreProducto("Mewtwo Base");

        Producto productoActualizado = new Producto();
        productoActualizado.setIdProducto(idProducto);
        productoActualizado.setNombreProducto("Mewtwo GX");

        ProductoResponse respuestaEsperada = new ProductoResponse(
                idProducto, "Mewtwo GX", "Carta rara holográfica", true, idProveedor, 30000D
        );

        Mockito.when(productoRepository.findById(idProducto)).thenReturn(Optional.of(productoExistente));
        Mockito.when(proveedorRepository.findById(idProveedor)).thenReturn(Optional.of(proveedorFalso)); //
        Mockito.when(productoRepository.save(productoExistente)).thenReturn(productoActualizado);
        Mockito.when(productoMapper.toResponse(productoActualizado)).thenReturn(respuestaEsperada);

        // 2. WHEN
        ProductoResponse resultado = productoService.modificarProducto(idProducto, request);

        // 3. THEN
        assertNotNull(resultado);
        assertEquals("Mewtwo GX", resultado.getNombreProducto());
        assertEquals(idProducto, resultado.getIdProducto());

        assertEquals("Mewtwo GX", productoExistente.getNombreProducto());
        assertEquals("Carta rara holográfica", productoExistente.getDescripcion());
        assertTrue(productoExistente.isEstadoActivo());

        Mockito.verify(productoRepository).save(productoExistente);
    }


    // TEST 4: desactivarProducto — producto inexistente → excepción

    @Test
    void desactivarProducto_debeLanzarExcepcion_CuandoProductoNoExiste() {
        // 1. GIVEN
        Long idInexistente = 999L;

        Mockito.when(productoRepository.existsById(idInexistente)).thenReturn(false);

        // 2. WHEN + 3. THEN
        ProductoInvalidoException excepcion = assertThrows(ProductoInvalidoException.class, () -> {
            productoService.desactivarProducto(idInexistente);
        });

        assertTrue(excepcion.getMessage().contains("no existe"));


        Mockito.verify(productoRepository, Mockito.never()).desactivarProducto(any());
    }
}