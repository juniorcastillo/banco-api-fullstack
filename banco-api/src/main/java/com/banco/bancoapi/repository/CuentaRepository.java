package com.banco.bancoapi.repository;

import com.banco.bancoapi.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);
    boolean existsByClienteId(Long clienteId);


}