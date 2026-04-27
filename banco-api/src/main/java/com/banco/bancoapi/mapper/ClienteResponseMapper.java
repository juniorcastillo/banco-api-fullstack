package com.banco.bancoapi.mapper;

import com.banco.bancoapi.dto.ClienteResponse;
import com.banco.bancoapi.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteResponseMapper {

    public ClienteResponse toDTO(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEstado()
        );
    }
}