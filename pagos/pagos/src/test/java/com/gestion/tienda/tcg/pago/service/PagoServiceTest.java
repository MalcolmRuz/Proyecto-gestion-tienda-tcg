package com.gestion.tienda.tcg.pago.service;

import com.gestion.tienda.tcg.pago.client.CarritoClient;
import com.gestion.tienda.tcg.pago.client.PedidoClient;
import com.gestion.tienda.tcg.pago.dto.CarritoItemResponse;
import com.gestion.tienda.tcg.pago.dto.CarritoResponse;
import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.enums.MetodoPago;
import com.gestion.tienda.tcg.pago.mapper.PagoMapper;
import com.gestion.tienda.tcg.pago.model.Pago;
import com.gestion.tienda.tcg.pago.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PagoMapper pagoMapper;

    @InjectMocks
    private PagoService pagoService;

    @Mock
    private CarritoClient carritoClient;

    @Mock
    private PedidoClient pedidoClient;

    @Test
    void realizarPagoCorrectamente() {

        PagoRequest request = new PagoRequest(
                1L,
                600000.0,
                MetodoPago.EFECTIVO,
                1L
        );

        CarritoItemResponse item = new CarritoItemResponse(
                1L,
                1L,
                "Charizard 1st Edition Shadowless",
                2,
                300000.0,
                600000.0,
                1L
        );

        CarritoResponse carrito = new CarritoResponse(
                1L,
                "PENDIENTE_VALIDACION",
                600000.0,
                List.of(item),
                "Santa Margarita 1383"
        );

        Pago pagoGuardado = new Pago();
        pagoGuardado.setIdPago(1L);

        PagoResponse response = new PagoResponse(
                1L,
                1L,
                600000.0,
                MetodoPago.EFECTIVO,
                EstadoPago.APROBADO,
                null,
                0.0
        );

        when(carritoClient.obtenerCarrito(1L)).thenReturn(carrito);

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        when(pagoMapper.toResponse(pagoGuardado)).thenReturn(response);

        PagoResponse resultado = pagoService.realizarPago(request);

        assertEquals(1L, resultado.getIdPago());

        verify(carritoClient).obtenerCarrito(1L);

        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void listarPagos() {

        Pago pago = new Pago();
        pago.setIdPago(1L);

        PagoResponse response = new PagoResponse(
                1L,
                10L,
                5000.0,
                MetodoPago.EFECTIVO,
                EstadoPago.APROBADO,
                null,
                0.0
        );

        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        when(pagoMapper.toResponse(pago)).thenReturn(response);

        List<PagoResponse> resultado = pagoService.listarPagos();

        assertEquals(1, resultado.size());

        verify(pagoRepository).findAll();
    }

    @Test
    void buscarPagoPorId() {

        Pago pago = new Pago();
        pago.setIdPago(1L);

        PagoResponse response = new PagoResponse(
                1L,
                10L,
                5000.0,
                MetodoPago.EFECTIVO,
                EstadoPago.APROBADO,
                null,
                0.0
        );

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        when(pagoMapper.toResponse(pago)).thenReturn(response);

        PagoResponse resultado = pagoService.buscarPagoPorId(1L);

        assertEquals(1L, resultado.getIdPago());

        verify(pagoRepository).findById(1L);
    }


    @Test
    void listarPagosAprobados() {

        Pago pago = new Pago();
        pago.setEstado(EstadoPago.APROBADO);

        PagoResponse response = new PagoResponse(
                1L,
                10L,
                5000.0,
                MetodoPago.EFECTIVO,
                EstadoPago.APROBADO,
                null,
                0.0
        );

        when(pagoRepository.findByEstado(EstadoPago.APROBADO)).thenReturn(List.of(pago));

        when(pagoMapper.toResponse(pago)).thenReturn(response);

        List<PagoResponse> resultado = pagoService.listarPagosPorEstado(EstadoPago.APROBADO);

        assertEquals(1, resultado.size());

        verify(pagoRepository).findByEstado(EstadoPago.APROBADO);
    }

}
