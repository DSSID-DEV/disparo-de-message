package com.dssiddev.disparowhatsapp.models;

import com.dssiddev.disparowhatsapp.models.dto.ContatoDTO;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "TB_CONTATOS")
public class Contato implements Serializable{
	
	private static final long serialVersionUID = 1595559797661247031L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "numero")
	private String cellPhone;

	private String nome;

	@Column(name = "ativo")
	private boolean ativo;

	public void formatarNome() {
		limparNome();
		verificarNome();
		formatarNomeComSobreNome();
	}

	private void formatarNomeComSobreNome() {
		String[] nomes = this.nome.split(Constantes.VAZIO);
		this.nome = nomes[0] + Constantes.VAZIO + nomes[nomes.length -1];
	}

	private void verificarNome() {
		this.nome = this.nome.matches("[a-zA-Z]+") ? this.nome : "";
	}

	private void limparNome() {
		this.nome = nome.replaceAll("[~!@#$%^&*()_-]+", "");
	}

    public ContatoDTO convertDTO() {
		return new ContatoDTO(this.id, this.nome, this.cellPhone);
    }

	public String tratarNome() {
		String[] nomes = this.nome.split(Constantes.VAZIO);
		int length = nomes.length -1;
		return nomes[0].concat(Constantes.VAZIO).concat(nomes[length]);
	}
}
