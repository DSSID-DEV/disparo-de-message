package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.models.Contato;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageWhatsapp {

	private String chatId;
	private String contentType;
	private Object content;

	public String parseJson() {
		return new StringBuilder()
				.append(Constantes.ABRE_CHAVE).append("\n")
				.append(linha(Constantes.CHAT_ID, this.chatId)).append(Constantes.VIRGULA).append("\n")
				.append(linha(Constantes.CONTENT_TYPE, this.contentType)).append(Constantes.VIRGULA).append("\n")
				.append(linha(Constantes.CONTENT, this.content.toString())).append("\n")
				.append(Constantes.FECHA_CHAVE).append("\n").toString();
	}

	private String linha(String field, String value) {
		return "	\"" + field + "\": " +" \"" + value +"\"";
	}

}
