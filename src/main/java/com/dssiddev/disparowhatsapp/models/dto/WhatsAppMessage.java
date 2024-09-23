package com.dssiddev.disparowhatsapp.models.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class WhatsAppMessage implements Serializable{

	private static final long serialVersionUID = 8685885003977519636L;
	
	private Long number;
	private String message;

}
