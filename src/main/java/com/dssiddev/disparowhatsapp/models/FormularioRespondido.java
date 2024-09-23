package com.dssiddev.disparowhatsapp.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_FORMALIOS_RESPONDIDOS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormularioRespondido implements Serializable {

    private static final long serialVersionUID = 5524863686729136647L;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chatId")
    private String chatId;
    @Column(name = "formulario_id")
    private Long formularioId;

    private Boolean respondido;


   public void verificarValorBooleano() {
       respondido = respondido == null ? false : respondido;
   }

    @Transient
    private Set<QuestaoRespondida> questoes = new HashSet<>();

}
