package com.gestion.tienda.tcg.carrito.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.dto.PagarCarritoRequest;
import com.gestion.tienda.tcg.carrito.service.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

        private final CarritoService carritoService;

        // Crear carrito
        @PostMapping
        public ResponseEntity<CarritoResponse> crearCarrito(
                        @Valid @RequestBody CarritoRequest request) {
                log.info("Creando un nuevo carrito de compras");
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(carritoService.crearCarrito(request));
        }

        // Listar carritos
        @GetMapping
        public ResponseEntity<List<CarritoResponse>> listar() {
                log.info("Listando todos los carritos");
                return ResponseEntity.ok(carritoService.listar());
        }

        // Buscar por ID
        @GetMapping("/{id}")
        public ResponseEntity<CarritoResponse> buscarPorId(@PathVariable @NonNull Long id) {
                log.info("Buscando carrito con ID: {}", id);
                return ResponseEntity.ok(carritoService.buscarPorId(id));
        }

        // SOLICITAR PAGO: Recibe el RequestBody para poder capturar la segunda
        // dirección
        @PostMapping("/{id}/pagar")
        public ResponseEntity<CarritoResponse> pagarCarrito(
                        @PathVariable @NonNull Long id,
                        @Valid @RequestBody PagarCarritoRequest request) {
                log.info("Solicitando pago del carrito con ID: {} para el usuario {}", id, request.getUsuarioId());
                return ResponseEntity.ok(carritoService.pagarCarrito(id, request));
        }

        // Actualizar estado de forma remota (Llamado por el orquestador de Pedidos)
        @PutMapping("/{id}/estado")
        public ResponseEntity<Void> actualizarEstado(
                        @PathVariable @NonNull Long id,
                        @RequestParam String nuevoEstado) {
                log.info("Actualizando estado de forma remota para carrito {} a {}", id, nuevoEstado);
                carritoService.actualizarEstadoExterno(id, nuevoEstado);
                return ResponseEntity.noContent().build();
        }

        // Confirmar pago aprobado (Invocado remotamente por el microservicio de Pago)
        @PutMapping("/{id}/confirmar-pago")
        public ResponseEntity<CarritoResponse> confirmarPago(
                        @PathVariable @NonNull Long id,
                        @Valid @RequestBody PagarCarritoRequest request) {
                log.info("Confirmando pago aprobado del carrito con ID: {}", id);

                return ResponseEntity.ok(
                                carritoService.confirmarPago(id, request.getUsuarioId()));
        }

        // Rechazar pago (Invocado remotamente por el microservicio de Pago)
        @PutMapping("/{id}/rechazar-pago")
        public ResponseEntity<CarritoResponse> rechazarPago(
                        @PathVariable @NonNull Long id,
                        @RequestParam(defaultValue = "false") boolean cancelar) {
                log.warn("Rechazando pago del carrito {}, cancelar={}", id, cancelar);
                return ResponseEntity.ok(carritoService.rechazarPago(id, cancelar));
        }

        // Cancelar carrito
        @PostMapping("/{id}/cancelar")
        public ResponseEntity<CarritoResponse> cancelarCarrito(@PathVariable @NonNull Long id) {
                log.warn("Cancelando carrito con ID: {}", id);
                return ResponseEntity.ok(carritoService.cancelarCarrito(id));
        }

        // Reactivar carrito
        @PostMapping("/{id}/reactivar")
        public ResponseEntity<CarritoResponse> reactivarCarrito(@PathVariable @NonNull Long id) {
                log.info("Reactivando carrito con ID: {}", id);
                return ResponseEntity.ok(carritoService.reactivarCarrito(id));
        }

        // Eliminar carrito (borrado físico de la base de datos)
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
                log.warn("Eliminando físicamente el carrito con ID: {}", id);
                carritoService.eliminar(id);
                return ResponseEntity.noContent().build();
        }
}