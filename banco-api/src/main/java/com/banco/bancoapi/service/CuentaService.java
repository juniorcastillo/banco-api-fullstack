package com.banco.bancoapi.service;

import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.exception.DatosInvalidosException;
import com.banco.bancoapi.exception.OperacionNoPermitidaException;
import com.banco.bancoapi.exception.RecursoNoEncontradoException;
import com.banco.bancoapi.repository.CuentaRepository;
import com.banco.bancoapi.repository.MovimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CuentaService {

    private static final Logger logger = LoggerFactory.getLogger(CuentaService.class);

    private final CuentaRepository cuentaRepository;
    private final ClienteService clienteService;
    private final MovimientoRepository movimientoRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteService clienteService, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteService = clienteService;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<Cuenta> listar() {
        logger.info("Listando todas las cuentas");
        return cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cuenta buscarPorId(Long id) {
        logger.info("Buscando cuenta por ID: {}", id);
        return cuentaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cuenta no encontrada con ID: {}", id);
                    return new RecursoNoEncontradoException("Cuenta no encontrada");
                });
    }

    public Cuenta crear(Long clienteId, Cuenta cuenta) {
        logger.info("Creando nueva cuenta para cliente ID: {} con número: {}", clienteId, cuenta.getNumeroCuenta());
        
        Cliente cliente = clienteService.buscarPorId(clienteId);

        if (cuentaRepository.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
            logger.warn("Intento de crear cuenta con número duplicado: {}", cuenta.getNumeroCuenta());
            throw new DatosInvalidosException("El número de cuenta ya existe");
        }
        
        if (!cliente.getEstado()) {
            logger.warn("Intento de crear cuenta para cliente inactivo. ID: {}", clienteId);
            throw new OperacionNoPermitidaException("El cliente está inactivo");
        }
        
        if (cuenta.getSaldoInicial() == null) {
            logger.warn("Saldo inicial no especificado para nueva cuenta");
            throw new DatosInvalidosException("El saldo inicial es obligatorio");
        }

        cuenta.setCliente(cliente);

        if (cuenta.getSaldoDisponible() == null) {
            cuenta.setSaldoDisponible(cuenta.getSaldoInicial());
        }

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        logger.info("Cuenta creada exitosamente con ID: {}", cuentaGuardada.getId());
        return cuentaGuardada;
    }

    public Cuenta actualizar(Long id, Cuenta datos) {
        logger.info("Actualizando cuenta con ID: {}", id);
        Cuenta cuenta = buscarPorId(id);

        if (!cuenta.getNumeroCuenta().equals(datos.getNumeroCuenta())
                && cuentaRepository.existsByNumeroCuenta(datos.getNumeroCuenta())) {
            logger.warn("Intento de actualizar con número de cuenta duplicado: {}", datos.getNumeroCuenta());
            throw new DatosInvalidosException("El número de cuenta ya existe");
        }

        cuenta.setNumeroCuenta(datos.getNumeroCuenta());
        cuenta.setTipoCuenta(datos.getTipoCuenta());
        cuenta.setSaldoInicial(datos.getSaldoInicial());
        cuenta.setEstado(datos.getEstado());

        logger.info("Cuenta actualizada exitosamente: {}", id);
        return cuentaRepository.save(cuenta);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando cuenta con ID: {}", id);
        Cuenta cuenta = buscarPorId(id);

        if (movimientoRepository.existsByCuentaId(id)) {
            logger.warn("Intento de eliminar cuenta con movimientos. ID: {}", id);
            throw new OperacionNoPermitidaException("No se puede eliminar una cuenta con movimientos");
        }
        
        cuentaRepository.delete(cuenta);
        logger.info("Cuenta eliminada exitosamente: {}", id);
    }
}