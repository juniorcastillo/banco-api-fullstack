package com.banco.bancoapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private Boolean estado;

    public ClienteResponse() {}

    public ClienteResponse(Long id, String nombre, String genero, Integer edad,
                           String identificacion, String direccion,
                           String telefono, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }
}