package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.CuentaDTO;
import com.banco.bancoapi.entity.Cuenta;
import com.banco.bancoapi.mapper.CuentaMapper;
import com.banco.bancoapi.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaService cuentaService;

    @MockBean
    private CuentaMapper cuentaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearCuenta() throws Exception {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNumeroCuenta("1234567890");
        cuentaDTO.setTipoCuenta("CORRIENTE");
        cuentaDTO.setSaldoInicial(new BigDecimal("1000"));
        cuentaDTO.setEstado(true);

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setTipoCuenta("CORRIENTE");
        cuenta.setSaldoInicial(new BigDecimal("1000"));
        cuenta.setEstado(true);

        when(cuentaMapper.toEntity(any(CuentaDTO.class))).thenReturn(cuenta);
        when(cuentaService.crear(eq(1L), any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDTO(any(Cuenta.class))).thenReturn(cuentaDTO);

        mockMvc.perform(post("/cuentas/cliente/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void deberiaFallarSiDatosInvalidos() throws Exception {
        CuentaDTO cuentaDTO = new CuentaDTO();

        mockMvc.perform(post("/cuentas/cliente/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuentaDTO)))
                .andExpect(status().is4xxClientError());
    }
}