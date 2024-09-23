package com.dssiddev.disparowhatsapp.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ResultadoColetado {

    private String opcao;
    private Integer escolhas;
    private double percentagem;

    public void calcularPercentagem(Integer total) {
        this.percentagem = escolhas != 0 ? (escolhas * 100) / total : 0;
    }
}
