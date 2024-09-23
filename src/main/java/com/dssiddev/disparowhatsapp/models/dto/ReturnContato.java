package com.dssiddev.disparowhatsapp.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnContato {

    private int qtdSalvos = 0;
    private String msgSalvos = " Salvos com sucesso!";

    private int qtdDescartado = 0;
    private String msgDescartado = " Descartados!";

    private Integer qtdEmBaseDeDados = 0;
    private String msgNaBaseDeDados = " Registros na base de dados!";

    private Integer qtdProcessada = 0;
    private String msgQtdProcessada = " Total de contatos Processados!";


    @JsonIgnore
    public void descartar() {
        this.qtdDescartado ++;
    }
    @JsonIgnore
    public void contabilizarSalvo() {
        this.qtdSalvos ++;
    }
    @JsonIgnore
    public void contarProcessados() {
        this.qtdProcessada ++;
    }
    @JsonIgnore
    public void finalizarProcessos() {
        this.setMsgSalvos(String.valueOf(this.qtdSalvos) + this.msgSalvos);
        this.setMsgNaBaseDeDados(String.valueOf(this.qtdEmBaseDeDados) + this.msgNaBaseDeDados);
        this.setMsgDescartado(String.valueOf(this.qtdDescartado) + this.msgDescartado);
        this.setMsgQtdProcessada(String.valueOf(this.qtdProcessada) + this.msgQtdProcessada);
    }

}
