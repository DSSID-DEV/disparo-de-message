package com.dssiddev.disparowhatsapp.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormularioRespondidoDTO {

    private Long id;
    private String chatId;
    private Long formularioId;
    private Long questaoId;
    private Long opcaoId;

}
