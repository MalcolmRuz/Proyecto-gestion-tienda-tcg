package com.gestion_tienda_tcg.productos.controller.v1;

import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PROOVEDOR",description = "Controller que se encarga del registro de proovedores")
@RequestMapping("/api/v1/proveedor")
public class ProveedorController {
    private final ProveedorService proveedorService;
    @Operation(summary = "Registro de proovedor",description = "A partir de un REQUEST crea un proovedor ")
    @PostMapping
    public ResponseEntity<ProveedorResponse> registrarProveedor(@RequestBody ProveedorRequest request) {
        log.info("REST request para registrar proveedor: {}", request.getNombreProveedor());
        var response = proveedorService.registrarProveedor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "Lista proveedores",description = "Lista todos los proveedores registrados")
    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> listarProveedores() {
        log.info("REST request para listar todos los proveedores");
        var lista = proveedorService.listarProveedores();
        return ResponseEntity.ok(lista);
    }
    @Operation(summary = "Actualiza el contacto del proveedor",description = "Segun ID edita el contacto registrado del proovedor.")
    @PatchMapping("/{id}/contacto")
    public ResponseEntity<ProveedorResponse> actualizarContacto(
            @PathVariable Long id,
            @RequestParam String nuevoContacto) {
        log.info("REST request para actualizar contacto del proveedor ID: {}", id);
        var response = proveedorService.actualizarContacto(id, nuevoContacto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Obtener proveedor por ID", description = "Retorna un proveedor individual buscado según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> obtenerProveedorPorId(@PathVariable Long id) {
        log.info("REST request  para obtener proveedor ID: {}", id);

        var response = proveedorService.obtenerProveedorPorID(id);
        return ResponseEntity.ok(response);
    }
}
