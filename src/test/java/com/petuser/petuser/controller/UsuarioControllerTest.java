package com.petuser.petuser.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.petuser.petuser.api.request.UsuarioLoginRequest;
import com.petuser.petuser.api.response.UsuarioGetResponse;
import com.petuser.petuser.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioGetResponse usuario1;
    private UsuarioGetResponse usuario2;

    @BeforeEach
    public void setUp() {
        usuario1 = new UsuarioGetResponse(null, null, null, null, null, null, null);
        usuario1.setNombre("Juan");
        usuario1.setApellido("Pérez");
        usuario1.setEmail("juan.perez@gmail.com");

        usuario2 = new UsuarioGetResponse(null, null, null, null, null, null, null);
        usuario2.setNombre("Carlos");
        usuario2.setApellido("López");
        usuario2.setEmail("carlos.lopez@gmail.com");
    }

    @Test
    public void getAllTest() throws Exception {
        when(usuarioService.getAllUsuarios()).thenReturn(List.of(usuario1, usuario2));
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Carlos"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"))
                .andExpect(jsonPath("$[1].apellido").value("López"))
                .andExpect(jsonPath("$[0].email").value("juan.perez@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("carlos.lopez@gmail.com"));
    }


    @Test
    public void getUsuarioByIdTest() throws Exception{
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuario1);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))   
                .andExpect(jsonPath("$.email").value("juan.perez@gmail.com"));
    }

    @Test
    public void getUsuarioByIdNotFoundTest() throws Exception {
        
        when(usuarioService.getUsuarioById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void loginTest() throws Exception{
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("juan.perez@gmail.com", "password123");
        when(usuarioService.login(loginRequest)).thenReturn(usuario1);
        mockMvc.perform(post("/api/v1/usuarios/login")
                .contentType("application/json")
                .content("{\"email\":\"juan.perez@gmail.com\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    @Test
    public void loginFailedTest() throws Exception{
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("juan.perez@gmail.com", "password123");
        when(usuarioService.login(loginRequest)).thenReturn(null);

        mockMvc.perform(post("/api/v1/usuarios/login")
                    .contentType("application/json")
                    .content("{\"email\":\"juan.perez@gmail.com\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUsuarioTest() throws Exception {
        long usuarioId = 1L;
        when(usuarioService.deleteUsuario(usuarioId)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/usuarios/" + usuarioId))
                .andExpect(status().isNoContent());
        verify(usuarioService, times(1)).deleteUsuario(usuarioId);
    }
}
