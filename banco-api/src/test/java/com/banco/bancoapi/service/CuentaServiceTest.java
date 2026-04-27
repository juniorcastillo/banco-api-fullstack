package com.banco.bancoapi.service;

import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.repository.CuentaRepository;
import com.banco.bancoapi.repository.MovimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    void deberiaCrearCuentaCorrectamente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setEstado(true);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("CORRIENTE");
        cuenta.setSaldoInicial(new BigDecimal("2000"));
        cuenta.setEstado(true);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cuenta resultado = cuentaService.crear(1L, cuenta);

        assertEquals(cliente, resultado.getCliente());
        assertEquals(new BigDecimal("2000"), resultado.getSaldoDisponible());
        assertEquals("478758", resultado.getNumeroCuenta());
        verify(cuentaRepository).save(any(Cuenta.class));
    }

    @Test
    void noDebeCrearCuentaConNumeroDuplicado() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("CORRIENTE");
        cuenta.setSaldoInicial(new BigDecimal("2000"));
        cuenta.setEstado(true);

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cuentaService.crear(1L, cuenta)
        );

        assertEquals("El número de cuenta ya existe", ex.getMessage());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void deberiaBuscarCuentaPorId() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("478758");

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        Cuenta resultado = cuentaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("478758", resultado.getNumeroCuenta());
    }

    @Test
    void debeLanzarErrorSiCuentaNoExiste() {
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cuentaService.buscarPorId(99L)
        );

        assertEquals("Cuenta no encontrada", ex.getMessage());
    }

    @Test
    void deberiaActualizarCuentaCorrectamente() {
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setId(1L);
        cuentaExistente.setNumeroCuenta("478758");
        cuentaExistente.setTipoCuenta("AHORRO");
        cuentaExistente.setSaldoInicial(new BigDecimal("1000"));
        cuentaExistente.setEstado(true);

        Cuenta datos = new Cuenta();
        datos.setNumeroCuenta("123456");
        datos.setTipoCuenta("CORRIENTE");
        datos.setSaldoInicial(new BigDecimal("2000"));
        datos.setEstado(false);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaExistente));
        when(cuentaRepository.existsByNumeroCuenta("123456")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cuenta resultado = cuentaService.actualizar(1L, datos);

        assertEquals("123456", resultado.getNumeroCuenta());
        assertEquals("CORRIENTE", resultado.getTipoCuenta());
        assertEquals(new BigDecimal("2000"), resultado.getSaldoInicial());
        assertFalse(resultado.getEstado());
    }

    @Test
    void noDebeActualizarConNumeroCuentaDuplicado() {
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setId(1L);
        cuentaExistente.setNumeroCuenta("478758");

        Cuenta datos = new Cuenta();
        datos.setNumeroCuenta("123456");
        datos.setTipoCuenta("CORRIENTE");
        datos.setSaldoInicial(new BigDecimal("2000"));
        datos.setEstado(true);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaExistente));
        when(cuentaRepository.existsByNumeroCuenta("123456")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cuentaService.actualizar(1L, datos)
        );

        assertEquals("El número de cuenta ya existe", ex.getMessage());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void noDebeEliminarCuentaConMovimientos() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.existsByCuentaId(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cuentaService.eliminar(1L)
        );

        assertEquals("No se puede eliminar una cuenta con movimientos", ex.getMessage());
        verify(cuentaRepository, never()).delete(any(Cuenta.class));
    }

    @Test
    void deberiaEliminarCuentaSinMovimientos() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.existsByCuentaId(1L)).thenReturn(false);

        cuentaService.eliminar(1L);

        verify(cuentaRepository).delete(cuenta);
    }
}