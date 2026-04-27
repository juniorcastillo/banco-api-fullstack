package com.banco.bancoapi.dto;

import com.banco.bancoapi.enums.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoDTO {

    private Long id;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El valor es obligatorio")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal valor;

    private BigDecimal saldo;
    private LocalDate fecha;

    private Long cuentaId;
    private String numeroCuenta;

    private Long clienteId;
    private String nombreCliente;

    public MovimientoDTO() {
    }

    public MovimientoDTO(
            Long id,
            TipoMovimiento tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo,
            LocalDate fecha,
            Long cuentaId,
            String numeroCuenta,
            Long clienteId,
            String nombreCliente
    ) {
        this.id = id;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.fecha = fecha;
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
    }

    public Long getId() {
        return id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}