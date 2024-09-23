package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.config.security.ConstantesValues;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtDTO {
    @NonNull
    private String token;
    private String type = ConstantesValues.TYPE_TOKEN;
    @NonNull
    private String username;
    @NonNull
    private String permissao;
}
