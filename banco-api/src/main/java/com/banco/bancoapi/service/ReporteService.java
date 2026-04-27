package com.banco.bancoapi.service;

import com.banco.bancoapi.dto.ReporteResponse;
import com.banco.bancoapi.entity.Movimiento;
import com.banco.bancoapi.exception.RecursoNoEncontradoException;
import com.banco.bancoapi.repository.MovimientoRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

    private final MovimientoRepository movimientoRepository;

    public ReporteService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerReporte(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        logger.info("Generando reporte para cliente ID: {} desde {} hasta {}", clienteId, fechaInicio, fechaFin);
        
        List<Movimiento> movimientos =
                movimientoRepository.findByCuentaClienteIdAndFechaBetween(
                        clienteId,
                        fechaInicio,
                        fechaFin
                );

        if (movimientos.isEmpty()) {
            logger.warn("No se encontraron movimientos para cliente {} en el rango especificado", clienteId);
        } else {
            logger.info("Se encontraron {} movimientos", movimientos.size());
        }

        return movimientos.stream()
                .map(m -> new ReporteResponse(
                        m.getFecha(),
                        m.getCuenta().getCliente().getNombre(),
                        m.getCuenta().getNumeroCuenta(),
                        m.getCuenta().getTipoCuenta(),
                        m.getCuenta().getSaldoInicial(),
                        m.getCuenta().getEstado(),
                        m.getValor(),
                        m.getSaldo()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public String obtenerReportePdfBase64(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        logger.info("Generando reporte PDF para cliente ID: {} desde {} hasta {}", clienteId, fechaInicio, fechaFin);
        
        List<ReporteResponse> reporte = obtenerReporte(clienteId, fechaInicio, fechaFin);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            document.add(new Paragraph("ESTADO DE CUENTA"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Cliente ID: " + clienteId));
            document.add(new Paragraph("Desde: " + fechaInicio));
            document.add(new Paragraph("Hasta: " + fechaFin));
            document.add(new Paragraph(" "));

            if (reporte.isEmpty()) {
                document.add(new Paragraph("No existen movimientos para el rango seleccionado."));
            }

            BigDecimal totalCreditos = BigDecimal.ZERO;
            BigDecimal totalDebitos = BigDecimal.ZERO;

            for (ReporteResponse r : reporte) {

                BigDecimal movimiento = r.getMovimiento();

                if (movimiento.compareTo(BigDecimal.ZERO) > 0) {
                    totalCreditos = totalCreditos.add(movimiento);
                } else {
                    totalDebitos = totalDebitos.add(movimiento.abs());
                }

                document.add(new Paragraph("Fecha: " + r.getFecha()));
                document.add(new Paragraph("Cliente: " + r.getCliente()));
                document.add(new Paragraph("Numero Cuenta: " + r.getNumeroCuenta()));
                document.add(new Paragraph("Tipo: " + r.getTipoCuenta()));
                document.add(new Paragraph("Saldo Inicial: " + r.getSaldoInicial()));
                document.add(new Paragraph("Estado: " + (Boolean.TRUE.equals(r.getEstado()) ? "Activa" : "Inactiva")));
                document.add(new Paragraph("Movimiento: " + r.getMovimiento()));
                document.add(new Paragraph("Saldo Disponible: " + r.getSaldoDisponible()));
                document.add(new Paragraph("-----------------------------------------"));
                document.add(new Paragraph(" "));
            }

            document.add(new Paragraph("RESUMEN"));
            document.add(new Paragraph("Total Créditos: " + totalCreditos));
            document.add(new Paragraph("Total Débitos: " + totalDebitos));
            document.add(new Paragraph(" "));

            document.close();

            String base64 = Base64.getEncoder().encodeToString(out.toByteArray());
            logger.info("Reporte PDF generado exitosamente");
            return base64;

        } catch (Exception e) {
            logger.error("Error generando PDF para cliente {}", clienteId, e);
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}