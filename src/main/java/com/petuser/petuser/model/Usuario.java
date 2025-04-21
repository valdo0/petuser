package com.petuser.petuser.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
