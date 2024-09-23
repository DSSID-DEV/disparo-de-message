package com.dssiddev.disparowhatsapp.services.constantes;

import java.util.List;
import java.util.regex.Pattern;

public class Constantes {

    public static final Pattern NAME_PATTERN = Pattern.compile("\\p{L}+\\s*");
    public static final Pattern PHONE_PATTERN = Pattern.compile("\\d+");

    public static final String REGEX_PARA_NOME = "^(?:s|[.]|[Nn][Aa][Mm][Ee]|[0-9]|\\p{Upper})+$";
    public static final String REGEX_PARA_CELLPHONE = "^(\\+\\d{2}\\s?)?(\\(?\\d{2}\\)?\\s?)?(\\d{4,5}\\-?\\d{4})$";
    public static final String VIRGULA = ",";
    public static final String VAZIO = "";
    public static final String COUNTRY = "+55";
    public static final String DDD = "81";
    public static final String NOVE = "9";
    public static final int CINCO = 5;
    public static final String CSV = "csv";
    public static final String TXT = "txt";
    public static final int POSICAO_NOME = 0;
    public static final int POSICAO_CELLPHONE = 1;
    public static final String QUEBRA_LINHA = "\n";
    public static final int MEIO_DIA = 12;
    public static final int DEZOITO_HORAS = 18;
    public static final int MINUTOS_SEGUNDOS = 00;
    public static final String ABRE_CHAVE = "{ ";
    public static final String FECHA_CHAVE = "}";
    public static final String CHAT_ID = "chatId";
    public static final String CONTENT_TYPE = "contentType";
    public static final String CONTENT = "content";
    public static final String MINE_TYPE = "minetype";
    public static final String DATA = "data";
    public static final String FILE_NAME = "filename";
    public static final String POLL_NAME = "pollName";
    public static final String POLL_OPTIONS = "pollOptions";
    public static final String OPTIONS = "options";
    public static final String ALLOW_MULTIPLE_ANSWERS = "allowMultipleAnswers";
    public static final String BOM_DIA = "Bom dia";
    public static final String BOA_TARDE = "Boa tarde";
    public static final String BOA_NOITE = "Boa noite";
    public static final String CUS = "@c.us";
    public static final String ALFABETO_MAIUSCULA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALFABETO_MINUSCULA = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMERICOS_CARACTERES_ESPECIAIS = "0123456789#$@&!S";
    public static final String CHARACTERS = ALFABETO_MAIUSCULA + ALFABETO_MINUSCULA + NUMERICOS_CARACTERES_ESPECIAIS;

    public static final String BASE_IMAGE_GIT = "https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/";

    public static final String SESSION_ID_DEFAULT = "DSSID_DEV";
    public static final List<String> PREFIXOS = List.of("Dr", "dr", "DR", "Dr.", "dr.", "DR.", "Dra", "dra", "DRA", "Dra.", "dra.", "DRA.", "psi", "abf", "ABF");
    public static final String SEM_NOME = "SEM NOME";
}