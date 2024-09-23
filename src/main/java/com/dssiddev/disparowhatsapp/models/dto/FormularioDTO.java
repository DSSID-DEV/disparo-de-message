package com.dssiddev.disparowhatsapp.models.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class FormularioDTO {

    private Long fomrularioId;
    private String municipio;
    private Set<QuestaoDTO> questoes = new HashSet<>();

}
