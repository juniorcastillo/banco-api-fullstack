package com.banco.bancoapi.mapper;

import com.banco.bancoapi.dto.CuentaDTO;
import com.banco.bancoapi.entity.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {
    
    public CuentaDTO toDTO(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }
        
        String nombreCliente = cuenta.getCliente() != null ? cuenta.getCliente().getNombre() : null;
        Long clienteId = cuenta.getCliente() != null ? cuenta.getCliente().getId() : null;
        
        return new CuentaDTO(
            cuenta.getId(),
            cuenta.getNumeroCuenta(),
            cuenta.getTipoCuenta(),
            cuenta.getSaldoInicial(),
            cuenta.getSaldoDisponible(),
            cuenta.getEstado(),
            clienteId,
            nombreCliente
        );
    }
    
    public Cuenta toEntity(CuentaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Cuenta cuenta = new Cuenta();
        cuenta.setId(dto.getId());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setSaldoDisponible(dto.getSaldoDisponible());
        cuenta.setEstado(dto.getEstado());
        
        return cuenta;
    }
}
