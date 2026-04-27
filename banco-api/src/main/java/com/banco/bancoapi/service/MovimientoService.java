package com.banco.bancoapi.service;

import com.banco.bancoapi.dto.MovimientoDTO;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.entity.Movimiento;
import com.banco.bancoapi.enums.TipoMovimiento;
import com.banco.bancoapi.exception.DatosInvalidosException;
import com.banco.bancoapi.exception.OperacionNoPermitidaException;
import com.banco.bancoapi.exception.RecursoNoEncontradoException;
import com.banco.bancoapi.repository.MovimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MovimientoService {

    private static final Logger logger = LoggerFactory.getLogger(MovimientoService.class);
    private static final BigDecimal CUPO_DIARIO = new BigDecimal("1000");

    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaService cuentaService) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
    }

    @Transactional(readOnly = true)
    public List<MovimientoDTO> listar() {
        logger.info("Listando todos los movimientos");

        return movimientoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoDTO buscarPorId(Long id) {
        logger.info("Buscando movimiento por ID: {}", id);

        Movimiento movimiento = buscarEntidadPorId(id);
        return convertirADTO(movimiento);
    }

    private Movimiento buscarEntidadPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Movimiento no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Movimiento no encontrado");
                });
    }

    public MovimientoDTO crear(Long cuentaId, Movimiento movimiento) {
        logger.info("Creando movimiento de tipo {} para cuenta ID: {}", movimiento.getTipoMovimiento(), cuentaId);

        Cuenta cuenta = cuentaService.buscarPorId(cuentaId);

        validarCuenta(cuenta);

        BigDecimal saldoActual = cuenta.getSaldoDisponible();
        BigDecimal valor = movimiento.getValor();

        if (saldoActual == null) {
            saldoActual = cuenta.getSaldoInicial();
        }

        BigDecimal nuevoSaldo = procesarMovimiento(cuentaId, movimiento, saldoActual, valor);

        cuenta.setSaldoDisponible(nuevoSaldo);

        movimiento.setFecha(LocalDate.now());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        logger.info("Movimiento creado exitosamente con ID: {}", movimientoGuardado.getId());

        return convertirADTO(movimientoGuardado);
    }

    private MovimientoDTO convertirADTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();

        dto.setId(movimiento.getId());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());
        dto.setFecha(movimiento.getFecha());

        Cuenta cuenta = movimiento.getCuenta();

        if (cuenta != null) {
            dto.setCuentaId(cuenta.getId());
            dto.setNumeroCuenta(cuenta.getNumeroCuenta());

            if (cuenta.getCliente() != null) {
                dto.setClienteId(cuenta.getCliente().getId());
                dto.setNombreCliente(cuenta.getCliente().getNombre());
            }
        }

        return dto;
    }

    private void validarCuenta(Cuenta cuenta) {
        if (cuenta.getCliente() == null) {
            logger.warn("La cuenta no tiene cliente asociado");
            throw new DatosInvalidosException("La cuenta no tiene cliente asociado");
        }

        if (Boolean.FALSE.equals(cuenta.getCliente().getEstado())) {
            logger.warn("Cliente inactivo. ID: {}", cuenta.getCliente().getId());
            throw new OperacionNoPermitidaException("El cliente está inactivo");
        }

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            logger.warn("Cuenta inactiva. ID: {}", cuenta.getId());
            throw new OperacionNoPermitidaException("La cuenta está inactiva");
        }
    }

    private BigDecimal procesarMovimiento(Long cuentaId, Movimiento movimiento, BigDecimal saldoActual, BigDecimal valor) {
        BigDecimal nuevoSaldo;

        if (movimiento.getTipoMovimiento() == TipoMovimiento.DEBITO) {
            validarDebito(cuentaId, saldoActual, valor);

            BigDecimal valorDebito = valor.abs();
            movimiento.setValor(valorDebito.negate());
            nuevoSaldo = saldoActual.subtract(valorDebito);

            logger.debug("Débito procesado: {} | Nuevo saldo: {}", valorDebito, nuevoSaldo);

        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.CREDITO) {
            BigDecimal valorCredito = valor.abs();
            movimiento.setValor(valorCredito);
            nuevoSaldo = saldoActual.add(valorCredito);

            logger.debug("Crédito procesado: {} | Nuevo saldo: {}", valorCredito, nuevoSaldo);

        } else {
            logger.error("Tipo de movimiento inválido: {}", movimiento.getTipoMovimiento());
            throw new DatosInvalidosException("Tipo de movimiento inválido");
        }

        return nuevoSaldo;
    }

    private void validarDebito(Long cuentaId, BigDecimal saldoActual, BigDecimal valor) {
        BigDecimal valorDebito = valor.abs();

        if (saldoActual.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Saldo no disponible para débito. Saldo actual: {}", saldoActual);
            throw new OperacionNoPermitidaException("Saldo no disponible");
        }

        if (saldoActual.compareTo(valorDebito) < 0) {
            logger.warn("Saldo insuficiente. Saldo: {} | Intento de débito: {}", saldoActual, valorDebito);
            throw new OperacionNoPermitidaException("Saldo no disponible");
        }

        BigDecimal totalDebitadoHoy = movimientoRepository.findByCuentaIdAndFecha(cuentaId, LocalDate.now())
                .stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.DEBITO)
                .map(m -> m.getValor().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebitadoHoy.add(valorDebito).compareTo(CUPO_DIARIO) > 0) {
            logger.warn("Cupo diario excedido. Total debitado hoy: {} | Intento de débito: {}", totalDebitadoHoy, valorDebito);
            throw new OperacionNoPermitidaException("Cupo diario excedido");
        }
    }

    public void eliminar(Long id) {
        logger.info("Eliminando movimiento con ID: {}", id);

        Movimiento movimiento = buscarEntidadPorId(id);

        movimientoRepository.delete(movimiento);

        logger.info("Movimiento eliminado exitosamente: {}", id);
    }
}