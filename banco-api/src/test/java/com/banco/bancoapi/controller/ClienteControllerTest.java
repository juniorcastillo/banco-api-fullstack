package com.banco.bancoapi.controller;

import com.banco.bancoapi.dto.ClienteCreateDTO;
import com.banco.bancoapi.dto.ClienteResponse;
import com.banco.bancoapi.entity.Cliente;
import com.banco.bancoapi.mapper.ClienteMapper;
import com.banco.bancoapi.mapper.ClienteResponseMapper;
import com.banco.bancoapi.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteMapper clienteMapper;

    @MockBean
    private ClienteResponseMapper clienteResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearCliente() throws Exception {

        ClienteCreateDTO clienteDTO = new ClienteCreateDTO();
        clienteDTO.setNombre("Jose Lema");
        clienteDTO.setGenero("MASCULINO");
        clienteDTO.setEdad(30);
        clienteDTO.setIdentificacion("1234567890");
        clienteDTO.setDireccion("Santo Domingo");
        clienteDTO.setTelefono("8091234567");
        clienteDTO.setPassword("123456");
        clienteDTO.setEstado(true);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("MASCULINO");
        cliente.setEdad(30);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Santo Domingo");
        cliente.setTelefono("8091234567");
        cliente.setPassword("123456");
        cliente.setEstado(true);

        ClienteResponse clienteResponse = new ClienteResponse(
                1L,
                "Jose Lema",
                "MASCULINO",
                30,
                "1234567890",
                "Santo Domingo",
                "8091234567",
                true
        );

        when(clienteMapper.toEntity(any(ClienteCreateDTO.class))).thenReturn(cliente);
        when(clienteService.crear(any(Cliente.class))).thenReturn(cliente);
        when(clienteResponseMapper.toDTO(any(Cliente.class))).thenReturn(clienteResponse);

        mockMvc.perform(post("/clientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void deberiaFallarSiDatosInvalidos() throws Exception {

        ClienteCreateDTO clienteDTO = new ClienteCreateDTO();

        mockMvc.perform(post("/clientes")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().is4xxClientError());
    }
}