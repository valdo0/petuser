package com.petuser.petuser.service;

import java.util.List;

import com.petuser.petuser.model.Usuario;

public interface UsuarioService {
    List<Usuario> getAllUsuarios();
    Usuario getUsuarioById(int id);
    Usuario addUsuario(Usuario usuario);
}
