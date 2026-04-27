package com.banco.bancoapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Persona {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "MASCULINO|FEMENINO", message = "El género debe ser MASCULINO o FEMENINO")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Column(unique = true)
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    private String telefono;
}