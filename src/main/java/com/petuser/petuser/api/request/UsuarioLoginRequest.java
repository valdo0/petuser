package com.petuser.petuser.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor

public class UsuarioLoginRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}
