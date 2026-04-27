package com.banco.bancoapi.entity;

import com.banco.bancoapi.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El valor es obligatorio")
    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(precision = 12, scale = 2)
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
}