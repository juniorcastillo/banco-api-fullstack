package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.CuentaDTO;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.mapper.CuentaMapper;
import com.banco.bancoapi.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin("*")
public class CuentaController {

    private final CuentaService cuentaService;
    private final CuentaMapper cuentaMapper;

    public CuentaController(CuentaService cuentaService, CuentaMapper cuentaMapper) {
        this.cuentaService = cuentaService;
        this.cuentaMapper = cuentaMapper;
    }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listar() {
        List<CuentaDTO> cuentas = cuentaService.listar().stream()
                .map(cuentaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> buscarPorId(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.buscarPorId(id);
        return ResponseEntity.ok(cuentaMapper.toDTO(cuenta));
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<CuentaDTO> crear(@PathVariable Long clienteId, @Valid @RequestBody CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
        Cuenta cuentaGuardada = cuentaService.crear(clienteId, cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaMapper.toDTO(cuentaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
        Cuenta cuentaActualizada = cuentaService.actualizar(id, cuenta);
        return ResponseEntity.ok(cuentaMapper.toDTO(cuentaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}