package com.dssiddev.disparowhatsapp.services.utils;

import com.dssiddev.disparowhatsapp.services.constantes.Constantes;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utils {
    private static final Pattern PATTERN = Pattern.compile("[*#]");
    private static final String COUNTRY = "+55";
    private static final String DDD = "81";
    private static final String NOVE = "9";
    private static final String VAZIO = "";
    private static final int CINCO = 5;

    public static String validarNumber(String number) {
       number = limparNumero(number);
       if(number.startsWith("3") || Utils.temCriteriosMinimosParaCelular(number)) return Constantes.VAZIO;
        if (verficarDDD(number)) {
            return Constantes.VAZIO;
        }
        number = obterPadraoNoveDigitos(number);
        number = adicionarCoutryDDD(number);
        return number;
    }

    private static boolean verficarDDD(String number) {
        number = number.replace("+", "").replace("55", "");
        if(number.length() <= 9) {
            return false;
        }
        if(!(number.startsWith("81") || number.startsWith("87"))) return false;
        return number.length() > 8 && !number.substring(0, 2).contains(Constantes.DDD);
    }

    private static String adicionarCoutryDDD(String number) {
        return Constantes.COUNTRY+Constantes.DDD+number;
    }

    private static String obterPadraoNoveDigitos(String number) {
        int countryComDDD = number.length() - 8;
        return number.substring(countryComDDD);
    }

    private static String limparNumero(String number) {

        String newNumber = number.replaceAll("[\\(\\)\\s\\-]", Constantes.VAZIO);
        return newNumber.substring(0, 1).equals("0") ? newNumber.substring(1) : newNumber;
    }

    public static boolean linhaValidaDoArquivoVCF(String linha) {
        return (linha.startsWith("FN") || linha.startsWith("TEL;CELL"));
    }

    public static boolean linhaValida(String line) {
       if (Utils.obterDado(line, Constantes.POSICAO_CELLPHONE) == null) {
           line.contains(Constantes.REGEX_PARA_CELLPHONE);
           return false;
       }
       Pattern pattern = Pattern.compile(Constantes.REGEX_PARA_CELLPHONE);
       Matcher matcherPhone = pattern.matcher(Utils.obterDado(line, Constantes.POSICAO_CELLPHONE));
       pattern = Pattern.compile(Constantes.REGEX_PARA_NOME);
       Matcher matcherName = pattern.matcher(Utils.obterDado(line, Constantes.POSICAO_NOME));
       return matcherPhone.matches() && !matcherName.matches();
    }

    public static boolean temCriteriosMinimosParaCelular(String line) {
        String numero = line.split(":").length > 1 ? line.split(":")[1] : "";

        if (numero.isBlank()) return false;

        if(PATTERN.matcher(numero).find()) return false;

        numero = Utils.limparNumero(numero);

        if(numero.startsWith("3")) return false;

        return numero.length() >= 8;
    }

    public static String obterDado(String line, int posicao) {
        if (line.split(",").length < posicao) {
            return null;
        }
        return line.split(",")[posicao];
    }
}
