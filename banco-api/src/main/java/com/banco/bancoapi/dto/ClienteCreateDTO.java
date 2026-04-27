package com.banco.bancoapi.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCreateDTO {

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

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}