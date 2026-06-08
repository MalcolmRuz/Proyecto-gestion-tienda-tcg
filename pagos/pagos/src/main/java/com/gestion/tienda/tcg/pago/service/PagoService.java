package com.gestion.tienda.tcg.pago.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.tienda.tcg.pago.client.CarritoClient;
import com.gestion.tienda.tcg.pago.dto.CarritoResponse;
import com.gestion.tienda.tcg.pago.dto.ConfirmarPagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoRequest;
import com.gestion.tienda.tcg.pago.dto.PagoResponse;
import com.gestion.tienda.tcg.pago.enums.EstadoPago;
import com.gestion.tienda.tcg.pago.exception.RecursoNoEncontradoException;
import com.gestion.tienda.tcg.pago.mapper.PagoMapper;
import com.gestion.tienda.tcg.pago.model.Pago;
import com.gestion.tienda.tcg.pago.repository.PagoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final CarritoClient carritoClient;

    public PagoService(PagoRepository pagoRepository,
            PagoMapper pagoMapper,
            CarritoClient carritoClient) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
        this.carritoClient = carritoClient;
    }

    @Transactional
    public PagoResponse realizarPago(PagoRequest request) {

        // 1. Obtener carrito y verificar que está en PENDIENTE_VALIDACION
        CarritoResponse carrito = carritoClient.obtenerCarrito(request.getIdCarrito());

        if (carrito == null) {
            throw new RecursoNoEncontradoException("Carrito no encontrado");
        }

        if (!"PENDIENTE_VALIDACION".equals(carrito.getEstadoCarrito())) {
            throw new IllegalStateException(
                    "El carrito no está en estado PENDIENTE_VALIDACION. " +
                            "Estado actual: " + carrito.getEstadoCarrito());
        }

        // 2. Calcular total desde los ítems del carrito
        Double totalCarrito = carrito.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();

        Double montoPago = request.getMonto();

        // 3. Comparar monto enviado por el cliente contra el total del carrito
        if (montoPago < totalCarrito) {
            // Pago rechazado: monto insuficiente
            // El carrito vuelve a PENDIENTE para que el cliente pueda reintentar
            log.warn("Pago rechazado para carrito {}: monto {} insuficiente, total {}",
                    request.getIdCarrito(), montoPago, totalCarrito);

            carritoClient.rechazarPago(request.getIdCarrito(), false);

            Pago pagoRechazado = new Pago();
            pagoRechazado.setIdCarrito(request.getIdCarrito());
            pagoRechazado.setMonto(montoPago);
            pagoRechazado.setEstado(EstadoPago.CANCELADO);
            pagoRechazado.setFechaPago(LocalDateTime.now());
            pagoRechazado.setMetodoPago(request.getMetodoPago());
            pagoRechazado.setVuelto(0.0);

            return pagoMapper.toResponse(pagoRepository.save(pagoRechazado));
        }

        // 4. Pago aprobado: notificar a Carrito para descontar stock, marcar PAGADO y
        // crear pedido
        log.info("Pago aprobado para carrito {}: monto {}, total {}",
                request.getIdCarrito(), montoPago, totalCarrito);

        Double vuelto = montoPago > totalCarrito ? montoPago - totalCarrito : 0.0;

        // Actualizado con Opción B: Se eliminó request.getDireccionEnvio()
        carritoClient.confirmarPago(
                request.getIdCarrito(),
                new ConfirmarPagoRequest(request.getUsuarioId()));

        Pago pagoAprobado = new Pago();
        pagoAprobado.setIdCarrito(request.getIdCarrito());
        pagoAprobado.setMonto(montoPago);
        pagoAprobado.setEstado(EstadoPago.APROBADO);
        pagoAprobado.setVuelto(vuelto);
        pagoAprobado.setFechaPago(LocalDateTime.now());
        pagoAprobado.setMetodoPago(request.getMetodoPago());

        return pagoMapper.toResponse(pagoRepository.save(pagoAprobado));
    }

    public List<PagoResponse> listarPagos() {
        return pagoRepository.findAll()
                .stream()
                .map(pagoMapper::toResponse)
                .toList();
    }
}