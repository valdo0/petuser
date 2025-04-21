package com.petuser.petuser.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioPatchRequest {

    @Size(max=50)
    private String nombre;

    @Size(max=50)
    private String apellido;

    @Email

    private String email;


    @Size(min=8, max=20)
    private String telefono;


    private String direccion;


    @Size(min=8, max=20)
    private String rol;

    private String password;
}
