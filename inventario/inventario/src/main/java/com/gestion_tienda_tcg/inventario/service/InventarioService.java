package com.gestion_tienda_tcg.inventario.service;

import com.gestion_tienda_tcg.inventario.client.ProductoClient;
import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.exception.ErrorInternoException;
import com.gestion_tienda_tcg.inventario.exception.InventarioInvalidoException;
import com.gestion_tienda_tcg.inventario.mapper.InventarioMapper;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final ProductoClient productoClient;
    public InventarioService(InventarioRepository inventarioRepository, InventarioMapper inventarioMapper,ProductoClient productoClient){
        this.inventarioRepository = inventarioRepository;
    this.inventarioMapper = inventarioMapper;
    this.productoClient = productoClient;

    }
    @Transactional
    public InventarioResponse agregar(InventarioRequest request) {
        log.info("Iniciando proceso de agregar nuevo inventario. Datos recibidos : {}", request);
        if(request.getStockActual() < 0){
            log.warn("Fallo al agregar inventario : Stock negativo ");
            throw new InventarioInvalidoException("El stock inicial no puede ser inferior a 0");
        }
        try {
            log.info("Consultando al microservicio de productos por el ID: {}", request.getIdProducto());


            var producto = productoClient.obtenerProductoPorId(request.getIdProducto());

            if (producto == null) {
                throw new InventarioInvalidoException("El producto no existe en el catálogo");
            }
        } catch (Exception e) {
            log.error("Error de interconexión o producto no encontrado: {}", e.getMessage());
            throw new InventarioInvalidoException("No se pudo validar el producto. Asegúrese de que el ID sea correcto.");
        }



        try {

            Inventario inventarioCreado = inventarioMapper.toEntity(request);
            Inventario inventarioGuardado = inventarioRepository.save(inventarioCreado);
            log.info("Inventario guardado exitosamente con ID: {}", inventarioGuardado.getIdInventario());
            return inventarioMapper.toResponse(inventarioGuardado);
        } catch (Exception e) {
            log.error("Error al intentar guardar el inventario: {}",e.getMessage());
            throw new ErrorInternoException("No se pudo completar el registro de inventario");
        }
    }
    //public InventarioResponse obtenerStockPorProducto(Long idProducto){}



    //public InventarioResponse aumentarStock(Long idProducto){}

    //public InventarioResponse disminuirStock(Long idProducto){}

    //public InventarioResponse ajustarStockFisico (InventarioRequest request) //<-------------ESTO ES UN UPDATE
    //public InventarioResponse eliminarInventario(InventarioRequest request)

    }


