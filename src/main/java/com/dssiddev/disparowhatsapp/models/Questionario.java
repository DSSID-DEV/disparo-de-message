package com.dssiddev.disparowhatsapp.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_QUESTIONARIOS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Questionario {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Conta conta;

    private LocalDateTime criadoEm;

    private String municipio;

    private String autorizacao;

    private String cliente;

    @OneToMany(mappedBy = "questionario", cascade = CascadeType.ALL)
    private List<Questao> questoes = new ArrayList<>();

}
