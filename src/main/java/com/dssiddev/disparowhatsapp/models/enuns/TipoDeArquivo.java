package com.dssiddev.disparowhatsapp.models.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoDeArquivo {
    TXT("txt"),
    CSV("csv"),
    VCF("vcf");

    private String descricao;

    public static TipoDeArquivo from(String tipo) {
        for (TipoDeArquivo t : TipoDeArquivo.values()) {
            if (t.getDescricao().equals(tipo)) {
                return t;
            }
        }
        return TipoDeArquivo.valueOf(tipo);
    }
}
