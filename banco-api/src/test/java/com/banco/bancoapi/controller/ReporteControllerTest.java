package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.ReporteResponse;
import com.banco.bancoapi.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Test
    void deberiaObtenerReporte() throws Exception {

        ReporteResponse r = new ReporteResponse(
                LocalDate.now(),
                "Jose Lema",
                "123456",
                "CORRIENTE",
                new BigDecimal("1000"),
                true,
                new BigDecimal("100"),
                new BigDecimal("1100")
        );

        when(reporteService.obtenerReporte(
                1L,
                LocalDate.parse("2026-04-25"),
                LocalDate.parse("2026-04-25")
        )).thenReturn(List.of(r));

        mockMvc.perform(get("/reportes")
                        .param("clienteId", "1")
                        .param("fechaInicio", "2026-04-25")
                        .param("fechaFin", "2026-04-25"))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaFallarSiFaltanParametros() throws Exception {

        mockMvc.perform(get("/reportes"))
                .andExpect(status().isBadRequest());
    }
}