package com.gestion.tienda.tcg.carrito.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

        private final CarritoRepository carritoRepository;
        private final CarritoMapper carritoMapper;
        private final CarritoHistorialService historialService;
        private final InventarioClient inventarioClient;

        // Crear carrito
        public CarritoResponse crearCarrito(CarritoRequest request) {
                log.info("Creando carrito");
                Carrito carrito = carritoMapper.toEntity(request);
                carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
                carrito.setTotalCarrito(0.0);

                Carrito guardado = carritoRepository.save(carrito);
                historialService.registrarHistorial(guardado, EstadoCarrito.ACTIVO, "Carrito creado");

                return carritoMapper.toResponse(guardado);
        }

        // Listar
        public List<CarritoResponse> listar() {
                return carritoRepository.findAll()
                                .stream()
                                .map(carritoMapper::toResponse)
                                .toList();
        }

        // Buscar por ID
        public CarritoResponse buscarPorId(@NonNull Long id) {
                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));
                return carritoMapper.toResponse(carrito);
        }

        // SOLICITAR PAGO: Guarda la segunda dirección opcional ingresada por el usuario
        @Transactional
        public CarritoResponse pagarCarrito(@NonNull Long id, PagarCarritoRequest request) {
                log.info("Solicitando pago del carrito {}", id);

                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() != EstadoCarrito.ACTIVO
                                && carrito.getEstadoCarrito() != EstadoCarrito.PENDIENTE) {
                        throw new BadRequestException(
                                        "Solo carritos en estado ACTIVO o PENDIENTE pueden solicitar pago");
                }

                if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
                        throw new BadRequestException("No se puede pagar un carrito vacío");
                }

                // Validar stock de todos los ítems antes de proceder
                for (var item : carrito.getItems()) {
                        InventarioResponse inventario;
                        try {
                                inventario = inventarioClient.obtenerInventarioPorProducto(item.getProductoId());
                        } catch (Exception e) {
                                carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE);
                                carritoRepository.save(carrito);
                                historialService.registrarHistorial(carrito, EstadoCarrito.PENDIENTE,
                                                "Error al consultar inventario del producto " + item.getProductoId());
                                throw new BadRequestException("Error al consultar inventario, intente nuevamente");
                        }

                        if (inventario.getStockActual() < item.getCantidad()) {
                                carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE);
                                carritoRepository.save(carrito);
                                historialService.registrarHistorial(carrito, EstadoCarrito.PENDIENTE,
                                                "Stock insuficiente para producto " + item.getProductoId()
                                                                + ": solicitado " + item.getCantidad()
                                                                + ", disponible " + inventario.getStockActual());
                                throw new BadRequestException(
                                                "Stock insuficiente para el producto " + item.getProductoId()
                                                                + ". Carrito marcado como PENDIENTE");
                        }
                }

                if (request.getDireccionEnvio() != null && !request.getDireccionEnvio().isBlank()) {
                        carrito.setDireccionEnvio(request.getDireccionEnvio());
                        log.info("Se ha asignado una dirección de envío específica para este pedido: {}",
                                        request.getDireccionEnvio());
                }

                carrito.setEstadoCarrito(EstadoCarrito.PENDIENTE_VALIDACION);
                Carrito guardado = carritoRepository.save(carrito);
                historialService.registrarHistorial(guardado, EstadoCarrito.PENDIENTE_VALIDACION,
                                "Stock validado, dirección asignada. Carrito en espera de validación de pago");

                return carritoMapper.toResponse(guardado);
        }

        // CONFIRMAR PAGO APROBADO: Mantiene la dirección previamente guardada en la
        // entidad Carrito
        @Transactional
        public CarritoResponse confirmarPago(@NonNull Long id, Long usuarioId) {
                log.info("Confirmando pago aprobado del carrito {}", id);

                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() != EstadoCarrito.PENDIENTE_VALIDACION) {
                        throw new BadRequestException(
                                        "Solo carritos en PENDIENTE_VALIDACION pueden confirmarse");
                }

                // Descontar stock
                for (var item : carrito.getItems()) {
                        try {
                                inventarioClient.disminuirStock(item.getProductoId(), item.getCantidad());
                                log.info("Stock descontado: producto {}, cantidad {}",
                                                item.getProductoId(), item.getCantidad());
                        } catch (Exception e) {
                                log.error("Error al descontar stock del producto {}: {}",
                                                item.getProductoId(), e.getMessage());
                                throw new BadRequestException("Error al descontar stock del producto "
                                                + item.getProductoId() + ", pago no confirmado");
                        }
                }

                carrito.setEstadoCarrito(EstadoCarrito.PAGADO);
                Carrito guardado = carritoRepository.save(carrito);
                historialService.registrarHistorial(guardado, EstadoCarrito.PAGADO, "Pago confirmado");

                return carritoMapper.toResponse(guardado);
        }

        // EXCLUSIVO PARA LA RED: Cambia el estado del carrito a petición de servicios
        // externos
        @Transactional
        public void actualizarEstadoExterno(@NonNull Long id, String nuevoEstado) {
                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));
                try {
                        EstadoCarrito estado = EstadoCarrito.valueOf(nuevoEstado.toUpperCase());
                        carrito.setEstadoCarrito(estado);
                        Carrito guardado = carritoRepository.save(carrito);
                        historialService.registrarHistorial(guardado, estado,
                                        "Estado modificado por el orquestador externo");
                        log.info("Estado del carrito {} cambiado a {} de manera remota", id, nuevoEstado);
                } catch (IllegalArgumentException e) {
                        throw new BadRequestException("Estado de carrito enviado no es válido: " + nuevoEstado);
                }
        }

        // RECHAZAR PAGO
        @Transactional
        public CarritoResponse rechazarPago(@NonNull Long id, boolean cancelar) {
                log.info("Rechazando pago del carrito {}", id);

                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() != EstadoCarrito.PENDIENTE_VALIDACION) {
                        throw new BadRequestException(
                                        "Solo carritos en PENDIENTE_VALIDACION pueden rechazarse");
                }

                EstadoCarrito nuevoEstado = cancelar ? EstadoCarrito.CANCELADO : EstadoCarrito.PENDIENTE;
                String descripcion = cancelar ? "Pago rechazado, carrito cancelado"
                                : "Pago rechazado, carrito disponible para reintento";

                carrito.setEstadoCarrito(nuevoEstado);
                Carrito guardado = carritoRepository.save(carrito);
                historialService.registrarHistorial(guardado, nuevoEstado, descripcion);

                return carritoMapper.toResponse(guardado);
        }

        // CANCELAR
        @Transactional
        public CarritoResponse cancelarCarrito(@NonNull Long id) {
                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO) {
                        throw new BadRequestException("No se puede cancelar un carrito pagado");
                }

                carrito.setEstadoCarrito(EstadoCarrito.CANCELADO);
                historialService.registrarHistorial(carrito, EstadoCarrito.CANCELADO, "Carrito cancelado");

                return carritoMapper.toResponse(carritoRepository.save(carrito));
        }

        // REACTIVAR
        @Transactional
        public CarritoResponse reactivarCarrito(@NonNull Long id) {
                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() != EstadoCarrito.PENDIENTE) {
                        throw new BadRequestException("Solo carritos pendientes pueden reactivarse");
                }

                carrito.setEstadoCarrito(EstadoCarrito.ACTIVO);
                historialService.registrarHistorial(carrito, EstadoCarrito.ACTIVO, "Carrito reactivado");

                return carritoMapper.toResponse(carritoRepository.save(carrito));
        }

        // ELIMINAR
        @Transactional
        public void eliminar(@NonNull Long id) {
                Carrito carrito = carritoRepository.findById(id)
                                .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));

                if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO) {
                        throw new BadRequestException("No se puede eliminar un carrito pagado");
                }

                historialService.registrarHistorial(carrito, EstadoCarrito.CANCELADO, "Carrito eliminado físicamente");
                carritoRepository.delete(carrito);
        }
}