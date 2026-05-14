package com.gestion_tienda_tcg.productos.service;


import com.gestion_tienda_tcg.productos.dto.ProductoRequest;
import com.gestion_tienda_tcg.productos.dto.ProductoResponse;
import com.gestion_tienda_tcg.productos.exception.ProductoInvalidoException;
import com.gestion_tienda_tcg.productos.mapper.ProductoMapper;
import com.gestion_tienda_tcg.productos.model.Producto;
import com.gestion_tienda_tcg.productos.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    private ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }
    public ProductoResponse guardarProducto(ProductoRequest request) {
        log.info("Recibiendo solicitud para guardar nuevo producto: {}", request.getNombre());

        Producto producto = productoMapper.toEntity(request);

        Producto productoGuardado = productoRepository.save(producto);
        log.info("Producto guardado exitosamente con ID: {}", productoGuardado.getIdProducto());

        return productoMapper.toResponse(productoGuardado);
    }
    @Transactional
    public ProductoResponse modificarProducto(Long id, ProductoRequest request) {
        log.info("Iniciando actualización para el producto ID: {}", id);

        return productoRepository.findById(id)
                .map(productoExistente -> {

                    productoExistente.setNombreProducto(request.getNombre());
                    productoExistente.setDescripcion(request.getDescripcion());
                    productoExistente.setEstadoActivo(request.getEstado());



                    Producto actualizado = productoRepository.save(productoExistente);
                    log.info("Producto ID: {} actualizado correctamente", id);
                    return productoMapper.toResponse(actualizado);
                })
                .orElseThrow(() -> {
                    log.error("Error al modificar: Producto ID {} no encontrado", id);
                    return new ProductoInvalidoException("No se puede editar un producto inexistente");
                });
    }

    public ProductoResponse obtenerDetalle(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoInvalidoException("Producto no encontrado"));
        return productoMapper.toResponse(producto);
    }

    @Transactional
    public void desactivarProducto(Long id) {
        log.info("Iniciando proceso de borrado lógico para el producto ID: {}", id);


        if (!productoRepository.existsById(id)) {
            log.error("Fallo al desactivar: Producto ID {} no encontrado", id);
            throw new ProductoInvalidoException("No se puede desactivar un producto que no existe");
        }

        try {
            productoRepository.desactivarProducto(id);
            log.info("Producto ID {} desactivado exitosamente", id);
        } catch (Exception e) {
            log.error("Error técnico al desactivar producto: {}", e.getMessage());
            throw new ProductoInvalidoException("Error al procesar la desactivación");
        }
    }


    public List<ProductoResponse> listarPorProveedor(Long idProveedor) {
        log.info("Consultando catálogo para el proveedor ID: {}", idProveedor);

        List<Producto> productos = productoRepository.findByProveedor_IdProveedor(idProveedor);

        if (productos.isEmpty()) {
            log.warn("El proveedor ID {} no tiene productos asociados", idProveedor);
            throw new ProductoInvalidoException("No se encontraron productos para este proveedor");
        }
        log.info("Se encontraron {} productos para el proveedor {}", productos.size(), idProveedor);
        return productos.stream()
                .map(productoMapper::toResponse)
                .toList();
    }

}
