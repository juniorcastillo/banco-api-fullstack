package com.banco.bancoapi.repository;

import com.banco.bancoapi.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdAndFecha(Long cuentaId, LocalDate fecha);
    List<Movimiento> findByCuentaClienteIdAndFechaBetween(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
    boolean existsByCuentaId(Long cuentaId);
}