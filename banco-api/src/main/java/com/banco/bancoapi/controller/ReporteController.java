package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.ReporteResponse;
import com.banco.bancoapi.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponse>> obtenerReporte(
            @RequestParam Long clienteId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        List<ReporteResponse> reporte = reporteService.obtenerReporte(
                clienteId,
                LocalDate.parse(fechaInicio),
                LocalDate.parse(fechaFin)
        );
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/pdf")
    public ResponseEntity<Map<String, String>> obtenerReportePdf(
            @RequestParam Long clienteId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        String pdfBase64 = reporteService.obtenerReportePdfBase64(
                clienteId,
                LocalDate.parse(fechaInicio),
                LocalDate.parse(fechaFin)
        );

        return ResponseEntity.ok(Map.of("pdfBase64", pdfBase64));
    }
}