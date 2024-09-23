package com.dssiddev.disparowhatsapp.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ResultadoDaQuestaoDTO {

    private String questao;
    private List<ResultadoColetado> resultadosColetados = new ArrayList<>();

}
