package com.petuser.petuser.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petuser.petuser.api.request.UsuarioCreateRequest;
import com.petuser.petuser.api.request.UsuarioLoginRequest;
import com.petuser.petuser.api.request.UsuarioPatchRequest;
import com.petuser.petuser.api.response.UsuarioGetResponse;
import com.petuser.petuser.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<EntityModel<UsuarioGetResponse>>> getAllUsuarios() {
        logger.info("Getting all users: {}");
        List<UsuarioGetResponse> usuarios = usuarioService.getAllUsuarios();
        logger.info("Usuarios: {}", usuarios.size());
        List<EntityModel<UsuarioGetResponse>> usuarioModels = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuarioModels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioGetResponse> createUsuario(@RequestBody UsuarioCreateRequest usuario) {
        logger.info("Creating user: {}", usuario);
        if (usuarioService.getUsuarioByEmail(usuario.getEmail()) != null) {
            logger.error("User already exists: {}", usuario);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!usuario.getRol().equals("conductor de transporte pet-friendly")
                && !usuario.getRol().equals("dueño de mascota")) {
            logger.error("Invalid role: {}", usuario);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UsuarioGetResponse usuarioResponse = usuarioService.addUsuario(usuario);
        if (usuarioResponse == null) {
            logger.error("Can't Create User: {}", usuario);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("User created: {}", usuarioResponse);
        EntityModel<UsuarioGetResponse> usuarioModel = EntityModel.of(usuarioResponse,
                linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).updateUser(usuarioResponse.getId(), null)).withRel("update"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(usuarioResponse.getId())).withRel("delete"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).login(null)).withRel("login"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).createUsuario(null)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(usuarioResponse.getId())).withRel("getById"));
        return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EntityModel<UsuarioGetResponse>> getUsuarioById(@PathVariable Long id) {
        logger.info("Getting user by id: {}", id);
        UsuarioGetResponse usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            logger.error("User not found: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("User found: {}", usuario);
        EntityModel<UsuarioGetResponse> usuarioModel = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).updateUser(id, null)).withRel("update"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(id)).withRel("delete"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).login(null)).withRel("login"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).createUsuario(null)).withRel("create"));
        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<UsuarioGetResponse>> login(@RequestBody UsuarioLoginRequest usuario) {
        logger.info("Logging in user: {}", usuario);
        UsuarioGetResponse usuarioResponse = usuarioService.login(usuario);
        if (usuarioResponse == null) {
            logger.error("User not found: {}", usuario);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("User logged in: {}", usuarioResponse);
        EntityModel<UsuarioGetResponse> usuarioModel = EntityModel.of(usuarioResponse,
                linkTo(methodOn(UsuarioController.class).login(null)).withSelfRel());

        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        logger.info("Deleting user: {}", id);
        if (usuarioService.deleteUsuario(id)) {
            logger.info("User deleted: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.error("User not found: {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<EntityModel<UsuarioGetResponse>> updateUser(@PathVariable Long id,
            @RequestBody UsuarioCreateRequest usuario) {
        logger.info("Updating user: {}", id);
        if (usuarioService.getUsuarioById(id) == null) {
            logger.error("User not found: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (usuarioService.getUsuarioByEmail(usuario.getEmail()) != null && !usuarioService.getUsuarioByEmail(usuario.getEmail()).getId().equals(id)) {
            logger.error("User already exists: {}", usuario);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!usuario.getRol().equals("conductor de transporte pet-friendly")
                && !usuario.getRol().equals("dueño de mascota")) {
            logger.error("Invalid role: {}", usuario);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UsuarioGetResponse user = usuarioService.updateUsuario(id, usuario);
        logger.info("User Updated: {}", user);
        EntityModel<UsuarioGetResponse> usuarioModel = EntityModel.of(user,
                linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withRel("getById"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).updateUser(id, null)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(id)).withRel("delete"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).login(null)).withRel("login"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).createUsuario(null)).withRel("create"));
        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @PatchMapping("/{id:\\d+}")
    public ResponseEntity<EntityModel<UsuarioGetResponse>> updateUsuario(@PathVariable Long id,
            @RequestBody UsuarioPatchRequest usuario) {
        logger.info("Updating user: {}", id);
        if (usuarioService.getUsuarioByEmail(usuario.getEmail()) != null) {
            logger.error("User already exists: {}", usuario);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (usuario.getRol() != null) {
            if (!usuario.getRol().equals("conductor de transporte pet-friendly")
                    && !usuario.getRol().equals("dueño de mascota")) {
                logger.error("Invalid role: {}", usuario);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (usuarioService.getUsuarioById(id) == null) {
            logger.error("User not found: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UsuarioGetResponse usuarioResponse = usuarioService.patchUsuario(id, usuario);
        logger.info("User updated: {}", usuarioResponse);
        EntityModel<UsuarioGetResponse> usuarioModel = EntityModel.of(usuarioResponse,
                linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("usuarios"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withRel("getById"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).updateUsuario(id, null)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(id)).withRel("delete"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).login(null)).withRel("login"));
        usuarioModel.add(linkTo(methodOn(UsuarioController.class).createUsuario(null)).withRel("create"));
        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

}
