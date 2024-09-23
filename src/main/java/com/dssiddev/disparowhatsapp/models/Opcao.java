package com.dssiddev.disparowhatsapp.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_OPCOES")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Opcao implements Serializable {

    private static final long serialVersionUID = -1552915602789839558L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "questao_id")
    private Questao questao;
}
