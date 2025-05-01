package com.petuser.petuser.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petuser.petuser.api.request.UsuarioCreateRequest;
import com.petuser.petuser.api.request.UsuarioLoginRequest;
import com.petuser.petuser.api.response.UsuarioGetResponse;
import com.petuser.petuser.model.Usuario;
import com.petuser.petuser.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    private UsuarioCreateRequest usuario;

    @BeforeEach
    public void setUp() {
        usuario = new UsuarioCreateRequest(null, null, null, null, null, null, null);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDireccion("123 Calle Principal");
        usuario.setTelefono("555-1234");
        usuario.setEmail("juan.perez@gmail.com");
        usuario.setPassword("password123");
        usuario.setRol("conductor de transporte pet-friendly");
    }

    @Test
    public void testCrearUsuario() {
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombre(usuario.getNombre());
        usuarioEntity.setApellido(usuario.getApellido());
        usuarioEntity.setDireccion(usuario.getDireccion());
        usuarioEntity.setTelefono(usuario.getTelefono());

        when(usuarioRepositoryMock.save(any(Usuario.class))).thenReturn(usuarioEntity);

        UsuarioGetResponse creado = usuarioService.addUsuario(usuario);

        assertNotNull(creado);
        assertEquals("Juan", creado.getNombre());
        verify(usuarioRepositoryMock, times(1)).save(any(Usuario.class));
    }

    @Test
    public void testBuscarUsuarioPorId() {
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombre("Juan");
        usuarioEntity.setApellido("Pérez");
        usuarioEntity.setDireccion("123 Calle Principal");
        usuarioEntity.setTelefono("555-1234");

        when(usuarioRepositoryMock.findById(1L)).thenReturn(Optional.of(usuarioEntity));

        UsuarioGetResponse encontrado = usuarioService.getUsuarioById(1L);

        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
        verify(usuarioRepositoryMock, times(1)).findById(1L);
    }

    @Test
    public void testEliminarUsuario() {

        when(usuarioRepositoryMock.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepositoryMock).deleteById(1L);
        usuarioService.deleteUsuario(1L);
        verify(usuarioRepositoryMock, times(1)).existsById(1L);
        verify(usuarioRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    public void testBuscarUsuarioPorEmail() {
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombre("Juan");
        usuarioEntity.setApellido("Pérez");
        usuarioEntity.setDireccion("123 Calle Principal");
        usuarioEntity.setTelefono("555-1234");
        usuarioEntity.setEmail("juan.perez@gmail.com");
        usuarioEntity.setPassword("password123");
        usuarioEntity.setRol("conductor de transporte pet-friendly");

        when(usuarioRepositoryMock.findByEmail("juan.perez@gmail.com")).thenReturn(usuarioEntity);

        UsuarioGetResponse encontrado = usuarioService.getUsuarioByEmail("juan.perez@gmail.com");

        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
        verify(usuarioRepositoryMock, times(1)).findByEmail("juan.perez@gmail.com");
    }

    @Test
    public void testGetAllUsuarios() {
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombre("Juan");
        usuarioEntity.setApellido("Pérez");
        List<Usuario> lista = List.of(usuarioEntity);

        when(usuarioRepositoryMock.findAll()).thenReturn(lista);
        List<UsuarioGetResponse> resultado = usuarioService.getAllUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        verify(usuarioRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testLoginUsuario() {
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setEmail("juan.perez@gmail.com");
        usuarioEntity.setPassword("password123");
        usuarioEntity.setNombre("Juan");

        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("juan.perez@gmail.com", "password123");

        when(usuarioRepositoryMock.findByEmail("juan.perez@gmail.com")).thenReturn(usuarioEntity);
        UsuarioGetResponse response = usuarioService.login(loginRequest);

        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        verify(usuarioRepositoryMock, times(1)).findByEmail("juan.perez@gmail.com");
    }

    @Test
    public void testLoginUsuarioNoExiste() {
        when(usuarioRepositoryMock.findByEmail("asd@asd.cl")).thenReturn(null);
    
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("asd@asd.cl", "1234");
        UsuarioGetResponse response = usuarioService.login(loginRequest);
        assertNull(response);;
    }

    @Test
    public void testUpdateUsuario() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNombre("Juan");

        when(usuarioRepositoryMock.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepositoryMock.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioCreateRequest updateRequest = new UsuarioCreateRequest(null, null, null, null, null, null, null);
        updateRequest.setNombre("Carlos");
        updateRequest.setApellido("López");
        updateRequest.setDireccion("Direccion 2.0");
        updateRequest.setTelefono("1111");
        updateRequest.setEmail("carlos.lopez@gmail.com");
        updateRequest.setPassword("nuevopass");
        updateRequest.setRol("dueño de mascota");

        UsuarioGetResponse actualizado = usuarioService.updateUsuario(1L, updateRequest);

        assertNotNull(actualizado);
        assertEquals("Carlos", actualizado.getNombre());
        assertEquals("López", actualizado.getApellido());
        assertEquals("Direccion 2.0", actualizado.getDireccion());
        assertEquals("1111", actualizado.getTelefono());
        assertEquals("carlos.lopez@gmail.com", actualizado.getEmail());
        assertEquals("dueño de mascota", actualizado.getRol());
        verify(usuarioRepositoryMock, times(1)).findById(1L);
        verify(usuarioRepositoryMock, times(1)).save(any(Usuario.class));
    }

}
