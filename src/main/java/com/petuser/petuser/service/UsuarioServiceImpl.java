package com.petuser.petuser.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petuser.petuser.api.request.UsuarioCreateRequest;
import com.petuser.petuser.api.request.UsuarioLoginRequest;
import com.petuser.petuser.api.request.UsuarioPatchRequest;
import com.petuser.petuser.api.response.UsuarioGetResponse;
import com.petuser.petuser.model.Usuario;
import com.petuser.petuser.repository.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioGetResponse> getAllUsuarios() {
        
        return usuarioRepository.findAll().stream()
                .map(usuario -> UsuarioGetResponse.builder()
                        .id(usuario.getId())
                        .nombre(usuario.getNombre())
                        .apellido(usuario.getApellido())
                        .email(usuario.getEmail())
                        .telefono(usuario.getTelefono())
                        .direccion(usuario.getDireccion())
                        .rol(usuario.getRol())
                        .build())
                .toList();
    }

    @Override
    public UsuarioGetResponse getUsuarioById(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            return UsuarioGetResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .direccion(usuario.getDireccion())
                    .rol(usuario.getRol())
                    .build();
        }
        return null;
    }

    @Override
    public UsuarioGetResponse addUsuario (UsuarioCreateRequest usuarioCreateRequest) {
        Usuario usuario = Usuario.builder()
                .nombre(usuarioCreateRequest.getNombre())
                .apellido(usuarioCreateRequest.getApellido())
                .email(usuarioCreateRequest.getEmail())
                .password(usuarioCreateRequest.getPassword())
                .telefono(usuarioCreateRequest.getTelefono())
                .direccion(usuarioCreateRequest.getDireccion())
                .rol(usuarioCreateRequest.getRol())
                .build();
        usuarioRepository.save(usuario);
        return UsuarioGetResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .rol(usuario.getRol())
                .build();
    }

    @Override
    public UsuarioGetResponse login(UsuarioLoginRequest usuarioLoginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(usuarioLoginRequest.getEmail());
        if (usuario != null && usuario.getPassword().equals(usuarioLoginRequest.getPassword())) {

            UsuarioGetResponse usuarioLoginResponse = UsuarioGetResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .direccion(usuario.getDireccion())
                    .rol(usuario.getRol())
                    .build();
            return usuarioLoginResponse;
        }
        return null;
    }
    @Override
    public UsuarioGetResponse getUsuarioByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            return UsuarioGetResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .direccion(usuario.getDireccion())
                    .rol(usuario.getRol())
                    .build();
        }
        return null;
    }
    @Override
    public boolean deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public UsuarioGetResponse updateUsuario(Long id, UsuarioCreateRequest usuarioUpdateRequest) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNombre(usuarioUpdateRequest.getNombre());
            usuario.setApellido(usuarioUpdateRequest.getApellido());
            usuario.setEmail(usuarioUpdateRequest.getEmail());
            usuario.setTelefono(usuarioUpdateRequest.getTelefono());
            usuario.setDireccion(usuarioUpdateRequest.getDireccion());
            usuario.setRol(usuarioUpdateRequest.getRol());
            usuario.setPassword(usuarioUpdateRequest.getPassword());
            usuarioRepository.save(usuario);
            return UsuarioGetResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .direccion(usuario.getDireccion())
                    .rol(usuario.getRol())
                    .build();
        }
        return null;
    }
    @Override
    public UsuarioGetResponse patchUsuario(Long id, UsuarioPatchRequest usuarioPatchRequest) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuarioPatchRequest.getNombre() != null) {
                usuario.setNombre(usuarioPatchRequest.getNombre());
            }
            if (usuarioPatchRequest.getApellido() != null) {
                usuario.setApellido(usuarioPatchRequest.getApellido());
            }
            if (usuarioPatchRequest.getEmail() != null) {
                usuario.setEmail(usuarioPatchRequest.getEmail());
            }
            if (usuarioPatchRequest.getTelefono() != null) {
                usuario.setTelefono(usuarioPatchRequest.getTelefono());
            }
            if (usuarioPatchRequest.getDireccion() != null) {
                usuario.setDireccion(usuarioPatchRequest.getDireccion());
            }
            if (usuarioPatchRequest.getRol() != null) {
                usuario.setRol(usuarioPatchRequest.getRol());
            }
            if (usuarioPatchRequest.getPassword() != null) {
                usuario.setPassword(usuarioPatchRequest.getPassword());
            }
            usuarioRepository.save(usuario);
            return UsuarioGetResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .direccion(usuario.getDireccion())
                    .rol(usuario.getRol())
                    .build();
        }
        return null;
    }

}
