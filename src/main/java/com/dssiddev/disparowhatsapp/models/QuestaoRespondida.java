package com.dssiddev.disparowhatsapp.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTOES_RESPONDIDAS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class QuestaoRespondida implements Serializable {

    private static final long serialVersionUID = -3854873603355135211L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "formulario_id")
    private Long formularioId;
    @Column(name = "questao_id")
    private Long questaoId;
    @Column(name = "opcao_id")
    private Long opcaoId;
}
