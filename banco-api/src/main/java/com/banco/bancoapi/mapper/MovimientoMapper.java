package com.banco.bancoapi.mapper;

import com.banco.bancoapi.dto.MovimientoDTO;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.entity.Movimiento;

public class MovimientoMapper {

    public static MovimientoDTO toDTO(Movimiento movimiento) {
        Cuenta cuenta = movimiento.getCuenta();

        return new MovimientoDTO(
                movimiento.getId(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getFecha(),
                cuenta != null ? cuenta.getId() : null,
                cuenta != null ? cuenta.getNumeroCuenta() : null,
                cuenta != null && cuenta.getCliente() != null ? cuenta.getCliente().getId() : null,
                cuenta != null && cuenta.getCliente() != null ? cuenta.getCliente().getNombre() : null
        );
    }
}