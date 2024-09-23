package com.dssiddev.disparowhatsapp.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTOES")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Questao implements Serializable {

    private static final long serialVersionUID = -2110473419730181683L;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questionario_id")
    private Questionario questionario;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL)
    private List<Opcao> opcoes = new ArrayList<>();
}
