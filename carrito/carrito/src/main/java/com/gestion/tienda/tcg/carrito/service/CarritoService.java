package com.gestion.tienda.tcg.carrito.service;

import com.gestion.tienda.tcg.carrito.Client.InventarioClient;
import com.gestion.tienda.tcg.carrito.dto.CarritoRequest;
import com.gestion.tienda.tcg.carrito.dto.CarritoResponse;
import com.gestion.tienda.tcg.carrito.enums.EstadoCarrito;
import com.gestion.tienda.tcg.carrito.exception.BadRequestException;
import com.gestion.tienda.tcg.carrito.exception.CarritoNotFoundException;
import com.gestion.tienda.tcg.carrito.mapper.CarritoMapper;
import com.gestion.tienda.tcg.carrito.model.Carrito;
import com.gestion.tienda.tcg.carrito.model.CarritoItem;
import com.gestion.tienda.tcg.carrito.repository.CarritoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoMapper carritoMapper;
    private final CarritoHistorialService historialService;
    private final InventarioClient inventarioClient;

    //=========================
    //Metodo para validar y crear nuevo carrito
    //=========================

    public CarritoResponse crearCarrito(CarritoRequest request){

        log.info("Creando el carrito"); //Mensaje de Estado
        Carrito carrito = carritoMapper.toEntity(request);
        carrito.setEstadoCarrito(EstadoCarrito.ACTIVO); //Por defecto, queda ACTIVO al momento de crear el carrito
        carrito.setTotalCarrito(0.0); //Por defecto, queda en 0.0 al momento de crear el carrito

        Carrito guardado = carritoRepository.save(carrito);

        //Luego de crear carrito, se registra en historial
        historialService.registrarHistorial(
                guardado,
                guardado.getEstadoCarrito(),"Carrito creado"
        );
        return carritoMapper.toResponse(guardado);
    }

    //=========================
    //Metodo para listar carritos
    //=========================
    public List<CarritoResponse> listar() {

        log.info("Listando todos los carritos");

        return carritoRepository.findAll()
                .stream()
                .map(carritoMapper::toResponse)
                .toList();
    }

    //=========================
    //Metodo para buscar carrito por ID
    //=========================

    public CarritoResponse buscarPorId(Long id) {

        log.info("Buscando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        return carritoMapper.toResponse(carrito);
    }
    //=========================
    //Metodo Actualizar Estado del carrito, buscandolo por ID
    //=========================
    @Transactional
    public CarritoResponse actualizarEstado(
            Long id,
            EstadoCarrito estado) {

        log.info("Actualizando estado del carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        //=========================
        //Validar los carritos pagados para no modificarlos
        //=========================
        if (carrito.getEstadoCarrito() == EstadoCarrito.PAGADO) {

            throw new BadRequestException(
                    "No se puede modificar un carrito pagado");
        }

        //=========================
        //Si el carrito esta en Estado "PAGADO"
        //=========================
        if (estado == EstadoCarrito.PAGADO) {
            // Valida si el carrito tiene items
            if (carrito.getItems() == null ||
                    carrito.getItems().isEmpty()) {

                throw new BadRequestException(
                        "No se puede pagar un carrito vacío");
            }

            //=========================
            //Si el carrito está PAGADO, verifica los items para disminuirlos en inventario
            //=========================
            for (CarritoItem item : carrito.getItems()) {

                try {
                    inventarioClient.disminuirStock(
                            item.getProductoId(),
                            item.getCantidad());
                } catch (Exception e) {
                    log.error(
                            "Error descontando stock del producto {}",
                            item.getProductoId());
                    throw new BadRequestException(
                            "Error al descontar stock en inventario");
                }
            }
        }

        carrito.setEstadoCarrito(estado);

        //=========================
        //Metodo para registrar carrito en el historial
        //=========================

        historialService.registrarHistorial(
                carrito,
                estado,
                "Estado actualizado a " + estado);

        return carritoMapper.toResponse(
                carritoRepository.save(carrito));
    }

    //=========================
    //Metodo para eliminar un carrito, buscandolo por ID
    //=========================
    @Transactional
    public void eliminar(Long id) {

        log.warn("Eliminando carrito {}", id);

        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(
                        "Carrito no encontrado"));

        if (!carrito.getItems().isEmpty()) {

            throw new BadRequestException(
                    "No se puede eliminar un carrito con items");
        }

        historialService.registrarHistorial(
                carrito,
                carrito.getEstadoCarrito(),
                "Carrito eliminado");

        carritoRepository.delete(carrito);
    }
}
