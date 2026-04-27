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

 public Cliente actualizar(Long id, Cliente cliente) {

    Cliente clienteExistente = buscarPorId(id);

    System.out.println("PASSWORD RECIBIDO = [" + cliente.getPassword() + "]");
    System.out.println("LONGITUD PASSWORD = " +
            (cliente.getPassword() != null ? cliente.getPassword().length() : "null"));

    clienteExistente.setNombre(cliente.getNombre());
    clienteExistente.setGenero(cliente.getGenero());
    clienteExistente.setEdad(cliente.getEdad());
    clienteExistente.setIdentificacion(cliente.getIdentificacion());
    clienteExistente.setDireccion(cliente.getDireccion());
    clienteExistente.setTelefono(cliente.getTelefono());
    clienteExistente.setEstado(cliente.getEstado());

    if (cliente.getPassword() != null && !cliente.getPassword().isBlank()) {
        clienteExistente.setPassword(cliente.getPassword());
    }

    return clienteRepository.save(clienteExistente);
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