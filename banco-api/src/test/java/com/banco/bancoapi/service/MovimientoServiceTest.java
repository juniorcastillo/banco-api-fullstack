package com.banco.bancoapi.service;

import com.banco.bancoapi.dto.MovimientoDTO;
import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.entity.Movimiento;
import com.banco.bancoapi.enums.TipoMovimiento;
import com.banco.bancoapi.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceTest {

    private MovimientoRepository movimientoRepository;
    private CuentaService cuentaService;
    private MovimientoService movimientoService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        movimientoRepository = mock(MovimientoRepository.class);
        cuentaService = mock(CuentaService.class);

        movimientoService = new MovimientoService(movimientoRepository, cuentaService);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEstado(true);

        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("123456");
        cuenta.setSaldoInicial(new BigDecimal("1000"));
        cuenta.setSaldoDisponible(new BigDecimal("1000"));
        cuenta.setEstado(true);
        cuenta.setCliente(cliente);
    }

    @Test
    void deberiaCrearMovimientoCredito() {

        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.CREDITO);
        movimiento.setValor(new BigDecimal("100"));

        when(cuentaService.buscarPorId(1L)).thenReturn(cuenta);

        when(movimientoRepository.save(any(Movimiento.class)))
                .thenAnswer(invocation -> {
                    Movimiento m = invocation.getArgument(0);
                    m.setId(1L);
                    return m;
                });

        MovimientoDTO resultado = movimientoService.crear(1L, movimiento);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(TipoMovimiento.CREDITO, resultado.getTipoMovimiento());
        assertEquals(new BigDecimal("100"), resultado.getValor());
        assertEquals(new BigDecimal("1100"), resultado.getSaldo());
        assertEquals("123456", resultado.getNumeroCuenta());
        assertEquals("Juan Pérez", resultado.getNombreCliente());
    }

    @Test
    void deberiaBuscarMovimientoPorId() {

        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setTipoMovimiento(TipoMovimiento.CREDITO);
        movimiento.setValor(new BigDecimal("100"));
        movimiento.setSaldo(new BigDecimal("1100"));
        movimiento.setFecha(LocalDate.now());
        movimiento.setCuenta(cuenta);

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        MovimientoDTO resultado = movimientoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("123456", resultado.getNumeroCuenta());
        assertEquals("Juan Pérez", resultado.getNombreCliente());
    }
}