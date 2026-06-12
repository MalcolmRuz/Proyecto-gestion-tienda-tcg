package com.gestion_tienda_tcg.productos.controller.v1;

import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PRODUCTO",description = "Controller que maneja la creación, edicion, busqueda de los productos del sistema")
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private final ProductoService productoService;
    @Operation(summary = "Creación de productos",description = "A partir de un request se crea un producto y se crea un inventario asociado al mismo.")
    @PostMapping
        public ResponseEntity<ProductoResponse> guardarProducto(@RequestBody ProductoRequest request) {
            log.info("REST request para guardar producto: {}", request.getNombreProducto());
            var response = productoService.guardarProducto(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        @Operation(summary = "Edición de productos", description = "Modifica un producto según su ID")
        @PutMapping("/{id}")
        public ResponseEntity<ProductoResponse> modificarProducto(
                @PathVariable Long id,
                @RequestBody ProductoRequest request) {
            log.info("REST request para modificar producto ID: {}", id);
            var response = productoService.modificarProducto(id, request);
            return ResponseEntity.ok(response);
        }
        @Operation(summary = "Busqueda de producto", description = "Retorna la informacion de un producto según su ID")
        @GetMapping("/{id}")
        public ResponseEntity<ProductoResponse> obtenerDetalle(@PathVariable Long id) {
            log.info("REST request para obtener detalle del producto ID: {}", id);
            var response = productoService.obtenerDetalle(id);
            return ResponseEntity.ok(response);
        }
        @Operation(summary = "Desactivación de un producto", description = "A partir de una ID, cambia el estado de un producto a INACTIVO")
        @PatchMapping("/{id}/desactivar")
        public ResponseEntity<Void> desactivarProducto(@PathVariable Long id) {
            log.info("REST request para desactivar producto ID: {}", id);
            productoService.desactivarProducto(id);
            return ResponseEntity.noContent().build();
        }
        @Operation(summary = "Listado de productos según proovedor",description = "Retorna una lista de productos según la ID de proveedor")
        @GetMapping("/proveedor/{idProveedor}")
        public ResponseEntity<List<ProductoResponse>> listarPorProveedor(@PathVariable Long idProveedor) {
            log.info("REST request para listar productos del proveedor ID: {}", idProveedor);
            var lista = productoService.listarPorProveedor(idProveedor);
            return ResponseEntity.ok(lista);
        }
    }




