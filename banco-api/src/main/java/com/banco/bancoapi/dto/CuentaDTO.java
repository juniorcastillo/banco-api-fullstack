package com.banco.bancoapi.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CuentaDTO {
    private Long id;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Pattern(regexp = "^[0-9]{6,10}$", message = "El número de cuenta debe tener entre 6 y 10 dígitos")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @PositiveOrZero(message = "El saldo no puede ser negativo")
    private BigDecimal saldoInicial;

    @PositiveOrZero(message = "El saldo disponible no puede ser negativo")
    private BigDecimal saldoDisponible;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    private Long clienteId;
    private String nombreCliente;

    public CuentaDTO() {
    }

    public CuentaDTO(Long id, String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, 
                     BigDecimal saldoDisponible, Boolean estado, Long clienteId, String nombreCliente) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoDisponible;
        this.estado = estado;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
