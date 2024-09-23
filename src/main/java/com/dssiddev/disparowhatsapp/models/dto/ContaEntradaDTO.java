package com.dssiddev.disparowhatsapp.models.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.tomcat.util.json.JSONParser;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaEntradaDTO {
    private Long id;
    private String descricao;
    private String telefone;
    private boolean ativa;
}
