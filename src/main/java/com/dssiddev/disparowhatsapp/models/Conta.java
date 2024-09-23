package com.dssiddev.disparowhatsapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONTAS")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Conta implements Serializable {
    private static final long serialVersionUID = 1843217374876081725L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao",  nullable = false, unique = true)
    private String descricao;

    private String telefone;

    private boolean ativa;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
