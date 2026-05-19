package com.gestion_tienda_tcg.inventario.service;

import com.gestion_tienda_tcg.inventario.client.ProductoClient;
import com.gestion_tienda_tcg.inventario.dto.InventarioDetalleResponse;
import com.gestion_tienda_tcg.inventario.dto.InventarioRequest;
import com.gestion_tienda_tcg.inventario.dto.InventarioResponse;
import com.gestion_tienda_tcg.inventario.dto.ProductoDto;
import com.gestion_tienda_tcg.inventario.enums.TipoMovimiento;
import com.gestion_tienda_tcg.inventario.exception.ErrorInternoException;
import com.gestion_tienda_tcg.inventario.exception.InventarioInvalidoException;
import com.gestion_tienda_tcg.inventario.mapper.InventarioMapper;
import com.gestion_tienda_tcg.inventario.model.Inventario;
import com.gestion_tienda_tcg.inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final ProductoClient productoClient;
    private final MovimientoStockService movimientoStockService;
    public InventarioService(InventarioRepository inventarioRepository, InventarioMapper inventarioMapper, ProductoClient productoClient, MovimientoStockService movimientoStockService){
        this.inventarioRepository = inventarioRepository;
    this.inventarioMapper = inventarioMapper;
    this.productoClient = productoClient;
    this.movimientoStockService = movimientoStockService;



    }

    public List<InventarioDetalleResponse> listarInventariosConProducto() {
        List<Inventario> inventarios = inventarioRepository.findAll();

        return inventarios.stream().map(inventario -> {
            String nombreProducto;

            try {
                ProductoDto producto = productoClient.obtenerProductoPorId(inventario.getIdProducto());

                nombreProducto = (producto != null && producto.getNombreProducto() != null)
                        ? producto.getNombreProducto()
                        : "Producto sin nombre";

            } catch (Exception e) {
                nombreProducto = "Producto no disponible (Error API)";
            }

            return inventarioMapper.toDetalleResponse(inventario, nombreProducto);
        }).collect(Collectors.toList());
    }




    @Transactional
    public InventarioResponse agregar(InventarioRequest request) {
        log.info("Iniciando proceso de agregar nuevo inventario. Datos recibidos : {}", request);
        if(request.getStockActual() < 0){
            log.warn("Fallo al agregar inventario : Stock negativo ");
            throw new InventarioInvalidoException("El stock inicial no puede ser inferior a 0");
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

    public InventarioResponse obtenerStockPorProducto(Long idProducto){
    log.info("Obteniendo  stock para el producto con ID: {}",idProducto);
    Inventario inventario = inventarioRepository.findByIdProducto(idProducto)
            .orElseThrow (() -> new InventarioInvalidoException("No se encontró inventario para el producto : " + idProducto));
        return inventarioMapper.toResponse(inventario);
    }


    @Transactional
    public InventarioResponse aumentarStock(Long idProducto, Integer cantidad) {
        log.info("Aumentando stock para el producto {}: +{}", idProducto, cantidad);

        Inventario inventario = inventarioRepository.findByIdProducto(idProducto)
                .orElseThrow(() -> new InventarioInvalidoException("No se puede aumentar stock de un producto no registrado"));

        int nuevoStock = inventario.getStockActual() + cantidad;
        inventario.setStockActual(nuevoStock);

        inventarioRepository.save(inventario);

        movimientoStockService.generarRegistro(
                inventario,
                cantidad,
                TipoMovimiento.ENTRADA
        );

        return inventarioMapper.toResponse(inventario);
    }


    @Transactional
    public InventarioResponse disminuirStock(Long idProducto, Integer cantidad) {
        log.info("Disminuyendo stock para el producto {}: {}", idProducto, cantidad);

        Inventario inventario = inventarioRepository.findByIdProducto(idProducto)
                .orElseThrow(() -> new InventarioInvalidoException("No se puede aumentar stock de un producto no registrado"));

        if (inventario.getStockActual() < cantidad) {
            throw new InventarioInvalidoException("Stock insuficiente. Disponible: "
                    + inventario.getStockActual() + ", Solicitado: " + cantidad);
        }

        int nuevoStock = inventario.getStockActual() - cantidad;
        inventario.setStockActual(nuevoStock);

        inventarioRepository.save(inventario);

        movimientoStockService.generarRegistro(
                inventario,
                cantidad * -1, //<-------para que se guarde como negativo en el registro
                TipoMovimiento.SALIDA
        );

        return inventarioMapper.toResponse(inventario);
    }


    @Transactional
    public InventarioResponse ajustarStockFisico(InventarioRequest request) {
        log.info("Ajuste manual de stock para el producto ID: {}", request.getIdProducto());


        Inventario inventario = inventarioRepository.findByIdProducto(request.getIdProducto())
                .orElseThrow(() -> new InventarioInvalidoException("No existe registro para ajustar"));

        inventario.setStockActual(request.getStockActual());
        inventarioRepository.save(inventario);


        movimientoStockService.generarRegistro(
                inventario,
                request.getStockActual(),
                TipoMovimiento.AJUSTE
        );

        return inventarioMapper.toResponse(inventario);
    }


    }


