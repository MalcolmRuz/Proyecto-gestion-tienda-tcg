package com.gestion_tienda_tcg.productos.service;

import com.gestion_tienda_tcg.productos.dto.CategoriaResponse;
import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.exception.CategoriaInvalidaException;
import com.gestion_tienda_tcg.productos.exception.ProveedorNoEncontradoException;
import com.gestion_tienda_tcg.productos.mapper.ProveedorMapper;
import com.gestion_tienda_tcg.productos.model.Categoria;
import com.gestion_tienda_tcg.productos.model.Proveedor;
import com.gestion_tienda_tcg.productos.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    public ProveedorResponse registrarProveedor(ProveedorRequest request) {
        log.info("Iniciando registro de proveedor: {}", request.getNombreProveedor());
        var proveedorParaGuardar = proveedorMapper.toEntity(request);
        var proveedorGuardado = proveedorRepository.save(proveedorParaGuardar);
        log.info("Proveedor guardado exitosamente con ID: {}", proveedorGuardado.getIdProveedor());
        return proveedorMapper.toResponse(proveedorGuardado);
    }

    public List<ProveedorResponse> listarProveedores() {
        log.info("Obteniendo lista de todos los proveedores");

        return proveedorRepository.findAll()
                .stream()
                .map(proveedorMapper::toResponse)
                .toList();
    }

    public ProveedorResponse actualizarContacto(Long idProveedor, String nuevoContacto) {
        log.info("Actualizando contacto para el proveedor ID: {}", idProveedor);
        var proveedorExistente = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + idProveedor));
        proveedorExistente.setContactoProveedor(nuevoContacto);

        // Guardamos los cambios (esto ejecuta un UPDATE en la BD)
        var proveedorActualizado = proveedorRepository.save(proveedorExistente);

        log.info("Contacto actualizado para: {}", proveedorActualizado.getNombreProveedor());

        return proveedorMapper.toResponse(proveedorActualizado);
    }

    public ProveedorResponse obtenerProveedorPorID(Long idProveedor) {
        log.info("Obteniendo informacion para proveedor con ID: {}", idProveedor);
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new ProveedorNoEncontradoException("No se encontró categoria con ID : " + idProveedor));
        return proveedorMapper.toResponse(proveedor);
    }
}
