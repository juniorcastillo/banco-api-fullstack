package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.MovimientoDTO;
import com.banco.bancoapi.entity.Movimiento;
import com.banco.bancoapi.enums.TipoMovimiento;
import com.banco.bancoapi.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimientoService movimientoService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void deberiaCrearMovimiento() throws Exception {

        Movimiento request = new Movimiento();
        request.setTipoMovimiento(TipoMovimiento.CREDITO);
        request.setValor(new BigDecimal("100"));

        MovimientoDTO response = new MovimientoDTO();
        response.setId(1L);
        response.setTipoMovimiento(TipoMovimiento.CREDITO);
        response.setValor(new BigDecimal("100"));
        response.setSaldo(new BigDecimal("1100"));
        response.setCuentaId(1L);
        response.setNumeroCuenta("123456");
        response.setClienteId(1L);
        response.setNombreCliente("Juan Pérez");

        when(movimientoService.crear(eq(1L), any(Movimiento.class)))
                .thenReturn(response);

        mockMvc.perform(post("/movimientos/cuenta/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    void deberiaFallarSiDatosInvalidos() throws Exception {

        MovimientoDTO movimientoDTO = new MovimientoDTO();

        mockMvc.perform(post("/movimientos/cuenta/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimientoDTO)))
        .andExpect(status().is4xxClientError());
        }
        }