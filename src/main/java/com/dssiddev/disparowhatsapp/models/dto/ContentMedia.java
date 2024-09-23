package com.dssiddev.disparowhatsapp.models.dto;


import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentMedia {

    private String mimetype;
    private String data;
    private String filename;


    public String parseJson() {
        return new StringBuilder()
                .append(Constantes.ABRE_CHAVE).append("\n")
                .append(linha(Constantes.MINE_TYPE, this.mimetype)).append(Constantes.VIRGULA).append("\n")
                .append(linha(Constantes.DATA, this.data)).append(Constantes.VIRGULA).append("\n")
                .append(linha(Constantes.FILE_NAME, this.filename.toString())).append("\n")
                .append(Constantes.FECHA_CHAVE).toString();
    }

    private String linha(String field, String value) {
        return "    \"" + field + "\": " +"\"" + value +"\"";
    }

}
