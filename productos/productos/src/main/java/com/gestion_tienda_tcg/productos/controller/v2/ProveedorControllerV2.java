package com.gestion_tienda_tcg.productos.controller.v2;

import com.gestion_tienda_tcg.productos.assemblers.ProveedorModelAssembler;
import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/proveedor")
@Tag(name = "PROOVEDOR V2", description = "API HATEOAS para el registro y gestión de proveedores usando EntityModel")
public class ProveedorControllerV2 {

    private final ProveedorService proveedorService;
    private final ProveedorModelAssembler assembler;

    @Operation(summary = "Obtener proveedor por ID", description = "Retorna un proveedor individual con sus enlaces de navegación")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProveedorResponse>> obtenerProveedorPorId(@PathVariable Long id) {
        log.info("REST request V2 para obtener proveedor ID: {}", id);
        // NOTA: Recuerda que si no tienes un buscarPorId en tu Service, deberás añadirlo
        // o adaptarlo para que el link self de HATEOAS sea completamente funcional.
        var response = proveedorService.obtenerProveedorPorID(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @Operation(summary = "Registro de proveedor", description = "Crea un proveedor y retorna el recurso enriquecido con hipermedios")
    @PostMapping
    public ResponseEntity<EntityModel<ProveedorResponse>> registrarProveedor(@RequestBody ProveedorRequest request) {
        log.info("REST request V2 para registrar proveedor: {}", request.getNombreProveedor());
        var response = proveedorService.registrarProveedor(request);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Lista proveedores", description = "Retorna una colección HAL con todos los proveedores registrados")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProveedorResponse>>> listarProveedores() {
        log.info("REST request V2 para listar todos los proveedores");
        var lista = proveedorService.listarProveedores();
        return ResponseEntity.ok(assembler.toCollectionModel(lista));
    }

    @Operation(summary = "Actualiza el contacto del proveedor", description = "Edita el contacto según ID y retorna el recurso actualizado con sus enlaces correspondientes")
    @PatchMapping("/{id}/contacto")
    public ResponseEntity<EntityModel<ProveedorResponse>> actualizarContacto(
            @PathVariable Long id,
            @RequestParam String nuevoContacto) {
        log.info("REST request V2 para actualizar contacto del proveedor ID: {}", id);
        var response = proveedorService.actualizarContacto(id, nuevoContacto);
        return ResponseEntity.ok(assembler.toModel(response));
    }
}
