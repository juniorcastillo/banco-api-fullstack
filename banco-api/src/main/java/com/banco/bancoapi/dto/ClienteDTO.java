package com.banco.bancoapi.dto;

import jakarta.validation.constraints.*;

public class ClienteDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]{10}$", message = "La identificación debe tener 10 dígitos")
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nombre, String genero, Integer edad, String identificacion, 
                      String direccion, String telefono, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
