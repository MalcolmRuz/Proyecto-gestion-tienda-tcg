package com.gestion.tienda.tcg.pedido.mapper;

import org.springframework.stereotype.Component;

import com.gestion.tienda.tcg.pedido.dto.EnvioRequest;
import com.gestion.tienda.tcg.pedido.dto.EnvioResponse;
import com.gestion.tienda.tcg.pedido.model.Envio;

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