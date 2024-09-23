package com.dssiddev.disparowhatsapp.utils;

import java.util.Base64;

public class EncoderAndDecoderBase64 {

    public static String encoderBase64(String value) {
        byte[] bytes = value.getBytes();
        String newValue = Base64.getEncoder().encodeToString(bytes);
        bytes = newValue.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String dencoderBase64(String value) {
        byte[] bytes = Base64.getDecoder().decode(value);
        String newValue = new String(bytes);
        bytes = Base64.getDecoder().decode(newValue);
        return new String(bytes);
    }
}
