package com.gestion.tienda.tcg.pedidos.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedidos.dto.EnvioRequest;
import com.gestion.tienda.tcg.pedidos.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedidos.model.Envio;

@Component
public class EnvioMapper {

    public Envio toEntity(EnvioRequest request) {

        Envio envio = new Envio();

        envio.setDireccionEnvio(request.getDireccionEnvio());

        return envio;
    }

    public EnvioResponse toResponse(Envio envio) {

        return new EnvioResponse(
                envio.getIdEnvio(),
                envio.getDireccionEnvio(),
                envio.getEstadoEnvio(),
                envio.getFechaEnvio());
    }
}