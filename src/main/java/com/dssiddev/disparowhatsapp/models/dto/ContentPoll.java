package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentPoll {
    private String pollName;
    private List<String> pollOptions = new ArrayList<>();
    private AllowMultipleAnsewers options;

    public String parseJson() {
        return new StringBuilder()
                .append(Constantes.ABRE_CHAVE).append("\n")
                .append(linha(Constantes.POLL_NAME, this.pollName)).append(Constantes.VIRGULA).append("\n")
                .append(percorrerLista(Constantes.POLL_OPTIONS, this.pollOptions)).append(Constantes.VIRGULA).append("\n")
                .append(newObjectJson(Constantes.OPTIONS, this.options.toString())).append("\n")
                .append(Constantes.FECHA_CHAVE).toString();
    }

    private String percorrerLista(String pollOptions, List<String> pollOptions1) {
        StringBuilder listOptions = new StringBuilder();
        listOptions.append("\"").append(pollOptions).append("\": [\n");
        int size = pollOptions.length() - 1;
        int indice = 0;
        for (String option: pollOptions1){
            listOptions.append("\"").append(option).append("\"");
            if (size < indice++) {
                listOptions.append(",");
            }
            listOptions.append("\n");
        }
        listOptions.append("],\n");
        return listOptions.toString();
    }

    private String newObjectJson(String options, String string) {
        return "\""+options+"\": {\n"+ string + "\n}";
    }

    private String linha(String field, String value) {
        return " \"" + field + " \": " +"\"" + value +"\"" + "\n";
    }

}
