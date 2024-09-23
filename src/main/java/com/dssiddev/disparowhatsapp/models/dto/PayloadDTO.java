package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.models.enuns.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PayloadDTO {
    private ContentType contentType;
    private MultipartFile file;
    private String titulo;
    private String mensagem;
    private String link;
    private String conclusao;

    public PayloadDTO(MultipartFile file, String titulo, String link, String conclusao) {
        this.file = file;
        this.titulo = titulo;
        this.link = link;
        this.conclusao = conclusao;
    }

    public PayloadDTO(ContentType contentType, String titulo, MultipartFile file, String mensagem, String link, String conclusao) {
        this.contentType = contentType;
        this.titulo = titulo;
        this.file = file;
        this.mensagem = mensagem;
        this.link = link;
        this.conclusao = conclusao;
    }

}

