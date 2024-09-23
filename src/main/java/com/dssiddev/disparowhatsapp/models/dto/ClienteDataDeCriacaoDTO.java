package com.dssiddev.disparowhatsapp.models.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class ClienteDataDeCriacaoDTO {

    private Long id;

    private LocalDateTime criadoEm;

    private String cliente;

}
