package com.petuser.petuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petuser.petuser.model.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

}
