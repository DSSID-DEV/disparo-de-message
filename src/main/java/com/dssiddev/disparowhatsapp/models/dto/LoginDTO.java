package com.dssiddev.disparowhatsapp.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank(message = "Informe o seu username ou email ou telefone!")
    private String username;
    @NotBlank(message = "O campo senha é obrigatório!")
    private String password;
}

