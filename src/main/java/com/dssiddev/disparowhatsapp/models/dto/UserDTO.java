package com.dssiddev.disparowhatsapp.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID userId;

    @NotBlank(message = "Informe o seu username ou email ou telefone!")
    private String username;

    @NotBlank(message = "O campo senha é obrigatório!")
    private String password;

}
