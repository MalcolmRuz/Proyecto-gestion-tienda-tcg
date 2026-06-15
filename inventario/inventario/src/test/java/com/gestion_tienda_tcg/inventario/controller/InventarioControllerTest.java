package com.gestion_tienda_tcg.inventario.controller;

import com.gestion_tienda_tcg.inventario.assemblers.InventarioDetalleModelAssembler;
import com.gestion_tienda_tcg.inventario.assemblers.InventarioModelAssembler;
import com.gestion_tienda_tcg.inventario.controller.v1.InventarioController;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(
        controllers = InventarioController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        }
)
public class InventarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;
    @MockitoBean
    private InventarioModelAssembler inventarioModelAssembler;
    @MockitoBean
    private InventarioDetalleModelAssembler inventarioDetalleModelAssembler;
    @MockitoBean
    private com.gestion_tienda_tcg.inventario.security.jwt.JwtService jwtService;

    @Test
    void stockPorProducto_DebeDevolver200_CuandoProductoExiste() throws Exception {
        Long idProducto = 100L;

        InventarioResponse respuestaSimulada = new InventarioResponse(1L, idProducto, 45, LocalDateTime.now());

        Mockito.when(inventarioService.obtenerStockPorProducto(idProducto))
                .thenReturn(respuestaSimulada);

        mockMvc.perform(get("/api/v1/inventarios/producto/{idProducto}", idProducto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(100))
                .andExpect(jsonPath("$.stockActual").value(45));
    }

    @Test
    void stockPorProducto_DebeDevolverError_CuandoProductoNoExiste() throws Exception {
        Long idProductoInexistente = 999L;

        Mockito.when(inventarioService.obtenerStockPorProducto(idProductoInexistente))
                .thenThrow(new RuntimeException("Producto no registrado en inventario"));

        mockMvc.perform(get("/api/v1/inventarios/producto/{idProducto}", idProductoInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
