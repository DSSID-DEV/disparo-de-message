package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContatoDTO{
	private Long id;
	private String nome;
	private String cellPhone;
}
