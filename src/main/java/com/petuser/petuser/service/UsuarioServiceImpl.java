package com.petuser.petuser.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.petuser.petuser.model.Usuario;


@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioServiceImpl(){
        usuarios.add(new Usuario(1, "Sebastián", "Valdivia", "se.valdivia@duocuc.cl", "12345678", "asd #123", "Dueño de Mascota"));
        usuarios.add(new Usuario(2, "Valentina", "Marchant", "valentina.marchant@gmail.com", "123456678", "asd #123","Conductor de Transporte Pet-Friendly"));
        usuarios.add(new Usuario(3, "Andres", "Valdivia", "andres.valdivia@gmail.com", "123456789", "asd #123", "Dueño de Mascota"));
    }
    @Override
    public List<Usuario> getAllUsuarios() {
        
        return usuarios; 
    }

    @Override
    public Usuario getUsuarioById(int id) {
        Usuario usuario = usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            return usuario;
        } else {
            return null;
        }
    }

    @Override
    public Usuario addUsuario (Usuario usuario) {
        usuarios.add(usuario);
        return usuario;
    }

}
