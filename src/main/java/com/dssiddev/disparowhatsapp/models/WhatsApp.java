package com.dssiddev.disparowhatsapp.models;

import java.io.Serializable;

public class WhatsApp implements Serializable{

	private static final long serialVersionUID = 2191398275258232169L;
	
	private String yourNumber;
	private String destiny;
	private String img;
	private String msg;
	
	public WhatsApp(String yourNumber, String destiny, String img, String msg) {
		super();
		this.yourNumber = yourNumber;
		this.destiny = destiny;
		this.img = img;
		this.msg = msg;
	}

	public String getYourNumber() {
		return yourNumber;
	}

	public void setYourNumber(String yourNumber) {
		this.yourNumber = yourNumber;
	}

	public String getDestiny() {
		return destiny;
	}

	public void setDestiny(String destiny) {
		this.destiny = destiny;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
