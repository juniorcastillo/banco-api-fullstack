package com.banco.bancoapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Column(unique = true)
    @Pattern(regexp = "\\d{6,10}", message = "El número de cuenta debe tener entre 6 y 10 dígitos")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
    @Column(precision = 12, scale = 2)
    private BigDecimal saldoInicial;

    @Column(precision = 12, scale = 2)
    private BigDecimal saldoDisponible;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @PrePersist
    public void prePersist() {
        if (saldoDisponible == null) {
            saldoDisponible = saldoInicial;
        }
    }
}