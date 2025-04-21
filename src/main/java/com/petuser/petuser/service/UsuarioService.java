package com.petuser.petuser.service;

import java.util.List;

import com.petuser.petuser.api.request.UsuarioCreateRequest;
import com.petuser.petuser.api.request.UsuarioLoginRequest;
import com.petuser.petuser.api.request.UsuarioPatchRequest;
import com.petuser.petuser.api.response.UsuarioGetResponse;

public interface UsuarioService {
    List<UsuarioGetResponse> getAllUsuarios();
    UsuarioGetResponse getUsuarioById(Long id);
    UsuarioGetResponse addUsuario(UsuarioCreateRequest usuario);
    UsuarioGetResponse login(UsuarioLoginRequest usuarioLoginRequest);
    UsuarioGetResponse getUsuarioByEmail(String email);
    boolean deleteUsuario(Long id);
    UsuarioGetResponse updateUsuario(Long id, UsuarioCreateRequest usuario);
    UsuarioGetResponse patchUsuario(Long id, UsuarioPatchRequest usuario);
}
