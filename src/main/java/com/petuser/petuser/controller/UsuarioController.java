package com.petuser.petuser.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petuser.petuser.model.Usuario;
import com.petuser.petuser.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }
    @PostMapping
    public Usuario postMethodName(@RequestBody Usuario usuario) {
        return usuarioService.addUsuario(usuario);
    }
    
    @GetMapping("/{id}")
    public Usuario getMethodName(@PathVariable int id) {
        return usuarioService.getUsuarioById(id);
    }
    

}
