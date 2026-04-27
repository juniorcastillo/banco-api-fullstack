package com.banco.bancoapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReporteResponse {

    private LocalDate fecha;
    private String cliente;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private BigDecimal movimiento;
    private BigDecimal saldoDisponible;

    public ReporteResponse(LocalDate fecha, String cliente, String numeroCuenta,
                           String tipoCuenta, BigDecimal saldoInicial, Boolean estado,
                           BigDecimal movimiento, BigDecimal saldoDisponible) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.movimiento = movimiento;
        this.saldoDisponible = saldoDisponible;
    }

    public LocalDate getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public Boolean getEstado() { return estado; }
    public BigDecimal getMovimiento() { return movimiento; }
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
}