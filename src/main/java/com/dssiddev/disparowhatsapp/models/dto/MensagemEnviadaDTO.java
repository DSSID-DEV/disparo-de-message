package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.models.Contato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemEnviadaDTO {

    private String saudacao;

    private String titulo;

    private String arquivo;

    private String mensagem;

    private String link;

    private String conclusao;

    private ContatoDTO contato;

    public void addContato(Contato contato) {
        this.setContato(contato.convertDTO());
    }
}
