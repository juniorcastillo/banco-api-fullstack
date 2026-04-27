package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.MovimientoDTO;
import com.banco.bancoapi.entity.Movimiento;
import com.banco.bancoapi.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public List<MovimientoDTO> listar() {
        return movimientoService.listar();
    }

    @GetMapping("/{id}")
    public MovimientoDTO buscarPorId(@PathVariable Long id) {
        return movimientoService.buscarPorId(id);
    }

    @PostMapping("/cuenta/{cuentaId}")
    public MovimientoDTO crear(
            @PathVariable Long cuentaId,
            @Valid @RequestBody Movimiento movimiento
    ) {
        return movimientoService.crear(cuentaId, movimiento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        movimientoService.eliminar(id);
    }
}