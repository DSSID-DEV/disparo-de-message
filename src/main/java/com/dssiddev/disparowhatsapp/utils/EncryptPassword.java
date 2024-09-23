package com.dssiddev.disparowhatsapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryptPassword {

    private final PasswordEncoder passwordEncoder;

    public EncryptPassword() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encryptPassword(String password) {
        return this.passwordEncoder.encode(password);
    }


    public boolean matches(String password, String enconderPassword) {
        return this.passwordEncoder.matches(password, enconderPassword);
    }

}
