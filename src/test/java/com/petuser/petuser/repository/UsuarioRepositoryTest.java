package com.petuser.petuser.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.petuser.petuser.model.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario crearUsuarioBase() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setDireccion("123 Calle Alameda");
        usuario.setTelefono("12345678");
        usuario.setEmail("asd@asd.cl");
        usuario.setPassword("password123");
        usuario.setRol("conductor de transporte pet-friendly");
        return usuario;
    }

    @DisplayName("Usuario Debería ser guardado correctamente")
    @Test
    public void guardarUsuarioTest() {
        Usuario usuario = crearUsuarioBase();
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        assertNotNull(usuarioGuardado.getId());
        assertEquals(usuario.getNombre(), usuarioGuardado.getNombre());
        assertEquals(usuario.getApellido(), usuarioGuardado.getApellido());
        assertEquals(usuario.getDireccion(), usuarioGuardado.getDireccion());
        assertEquals(usuario.getTelefono(), usuarioGuardado.getTelefono());
        assertEquals(usuario.getEmail(), usuarioGuardado.getEmail());
        assertEquals(usuario.getPassword(), usuarioGuardado.getPassword());
        assertEquals(usuario.getRol(), usuarioGuardado.getRol());
    }

    @DisplayName("Usuario debe ser actualizado correctamente")
    @Test
    public void updateUsuarioTest() {
        Usuario usuario = crearUsuarioBase();
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        usuarioGuardado.setNombre("Carlos");
        usuarioGuardado.setApellido("Gómez");
        usuarioGuardado.setDireccion("456 Calle Pajaritos");
        usuarioGuardado.setTelefono("87654321");
        usuarioGuardado.setEmail("carlos@correo.com");
        usuarioGuardado.setPassword("newpassword456");
        usuarioGuardado.setRol("dueño de mascota");

        Usuario usuarioActualizado = usuarioRepository.save(usuarioGuardado);

        assertNotNull(usuarioActualizado.getId());
        assertEquals("Carlos", usuarioActualizado.getNombre());
        assertEquals("Gómez", usuarioActualizado.getApellido());
        assertEquals("456 Calle Pajaritos", usuarioActualizado.getDireccion());
        assertEquals("87654321", usuarioActualizado.getTelefono());
        assertEquals("carlos@correo.com", usuarioActualizado.getEmail());
        assertEquals("newpassword456", usuarioActualizado.getPassword());
        assertEquals("dueño de mascota", usuarioActualizado.getRol());
    }

    @DisplayName("Usuario debe ser eliminado correctamente")
    @Test
    public void deleteUsuarioTest() {
        Usuario usuario = crearUsuarioBase();
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        assertNotNull(usuarioGuardado.getId());
        usuarioRepository.delete(usuarioGuardado);

        Optional<Usuario> usuarioEliminado = usuarioRepository.findById(usuarioGuardado.getId());
        assertTrue(usuarioEliminado.isEmpty());
    }

    @DisplayName("Usuario debe ser encontrado por ID")
    @Test
    public void buscarUsuarioPorIdTest() {
        Usuario usuario = crearUsuarioBase();
        Usuario guardado = usuarioRepository.save(usuario);

        Usuario encontrado = usuarioRepository.findById(guardado.getId()).orElse(null);

        assertNotNull(encontrado);
        assertEquals(usuario.getNombre(), encontrado.getNombre());
    }

    @DisplayName("Debe aumentar el conteo de usuarios al guardar uno nuevo")
    @Test
    public void conteoUsuariosTest() {
        long countAntes = usuarioRepository.count();
        usuarioRepository.save(crearUsuarioBase());
        long countDespues = usuarioRepository.count();

        assertEquals(countAntes + 1, countDespues);
    }

    @DisplayName("Debe lanzar excepción al guardar usuario con email duplicado (si hay restricción única)")
    @Test
    public void guardarUsuarioConEmailDuplicadoTest() {
        Usuario usuario1 = crearUsuarioBase();
        Usuario usuario2 = crearUsuarioBase();

        usuarioRepository.save(usuario1);
        assertThrows(Exception.class, () -> usuarioRepository.save(usuario2));
    }

    @DisplayName("Debe lanzar excepción al guardar usuario con nombre nulo (si es obligatorio)")
    @Test
    public void guardarUsuarioConNombreNuloTest() {
        Usuario usuario = crearUsuarioBase();
        usuario.setNombre(null);

        assertThrows(Exception.class, () -> usuarioRepository.save(usuario));
    }
}
