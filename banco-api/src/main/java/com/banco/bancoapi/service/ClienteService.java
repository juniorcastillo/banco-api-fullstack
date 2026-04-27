package com.banco.bancoapi.service;

import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.exception.OperacionNoPermitidaException;
import com.banco.bancoapi.exception.RecursoNoEncontradoException;
import com.banco.bancoapi.repository.ClienteRepository;
import com.banco.bancoapi.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public ClienteService(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        logger.info("Listando todos los clientes");
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        logger.info("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cliente no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Cliente no encontrado");
                });
    }

    public Cliente crear(Cliente cliente) {
        logger.info("Creando nuevo cliente con identificación: {}", cliente.getIdentificacion());
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        logger.info("Actualizando cliente con ID: {}", id);
        Cliente cliente = buscarPorId(id);

        cliente.setNombre(datos.getNombre());
        cliente.setGenero(datos.getGenero());
        cliente.setEdad(datos.getEdad());
        cliente.setIdentificacion(datos.getIdentificacion());
        cliente.setDireccion(datos.getDireccion());
        cliente.setTelefono(datos.getTelefono());
        cliente.setEstado(datos.getEstado());

        logger.info("Cliente actualizado exitosamente: {}", id);
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando cliente con ID: {}", id);
        Cliente cliente = buscarPorId(id);

        if (cuentaRepository.existsByClienteId(id)) {
            logger.warn("Intento de eliminar cliente con cuentas asociadas. ID: {}", id);
            throw new OperacionNoPermitidaException("No se puede eliminar el cliente porque tiene cuentas asociadas");
        }

        clienteRepository.delete(cliente);
        logger.info("Cliente eliminado exitosamente: {}", id);
    }
}