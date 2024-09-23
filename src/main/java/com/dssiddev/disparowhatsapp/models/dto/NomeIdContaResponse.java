package com.dssiddev.disparowhatsapp.models.dto; 


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class NomeIdContaResponse {

    private Long id;

    private LocalDateTime criadoEm;

    private String municipio;

    private String cliente;

    private String conta;

}