package com.petuser.petuser.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioCreateRequest {
    @NotNull
    @Size(max=50)
    private String nombre;

    @Size(max=50)
    private String apellido;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min=8, max=20)
    private String telefono;

    @NotNull
    private String direccion;

    @NotNull
    @Size(min=8, max=40)
    private String rol;

    @NotNull
    private String password;
}
