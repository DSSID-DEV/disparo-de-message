package com.dssiddev.disparowhatsapp.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeResponse {
    private MultipartFile qrCode;
    private int statusCode;
}
