package com.dssiddev.disparowhatsapp.models.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private String telefone;
    private String permissao;
    private String contas;
}
