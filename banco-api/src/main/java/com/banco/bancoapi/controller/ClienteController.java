package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.ClienteCreateDTO;
import com.banco.bancoapi.dto.ClienteDTO;
import com.banco.bancoapi.dto.ClienteResponse;
import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.mapper.ClienteMapper;
import com.banco.bancoapi.mapper.ClienteResponseMapper;
import com.banco.bancoapi.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    private final ClienteResponseMapper clienteResponseMapper;

    public ClienteController(ClienteService clienteService,
                             ClienteMapper clienteMapper,
                             ClienteResponseMapper clienteResponseMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
        this.clienteResponseMapper = clienteResponseMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> clientes = clienteService.listar().stream()
                .map(clienteResponseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(clienteResponseMapper.toDTO(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteCreateDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente clienteGuardado = clienteService.crear(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteResponseMapper.toDTO(clienteGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ClienteCreateDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente clienteActualizado = clienteService.actualizar(id, cliente);

        return ResponseEntity.ok(clienteResponseMapper.toDTO(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}