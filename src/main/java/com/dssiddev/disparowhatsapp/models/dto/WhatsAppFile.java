package com.dssiddev.disparowhatsapp.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class WhatsAppFile {

	private Long number;
	private MultipartFile file;

}
