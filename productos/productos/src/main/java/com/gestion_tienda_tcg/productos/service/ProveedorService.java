package com.gestion_tienda_tcg.productos.service;

import com.gestion_tienda_tcg.productos.dto.ProveedorRequest;
import com.gestion_tienda_tcg.productos.dto.ProveedorResponse;
import com.gestion_tienda_tcg.productos.mapper.ProveedorMapper;
import com.gestion_tienda_tcg.productos.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    private ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

//public ProveedorResponse registrarProveedor(ProveedorRequest request){}


// public List<ProveedorResponse> listarProveedores (){}


// public ProveedorResponse actualizarContacto(Long idProveedor, String contacto){}


}
