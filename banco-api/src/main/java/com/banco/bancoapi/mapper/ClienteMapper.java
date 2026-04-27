package com.banco.bancoapi.mapper;

import com.banco.bancoapi.dto.ClienteCreateDTO;
import com.banco.bancoapi.dto.ClienteDTO;
import com.banco.bancoapi.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteCreateDTO dto) {
        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setPassword(dto.getPassword());
        cliente.setEstado(dto.getEstado());

        return cliente;
    }

    public Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEstado(dto.getEstado());

        return cliente;
    }
}