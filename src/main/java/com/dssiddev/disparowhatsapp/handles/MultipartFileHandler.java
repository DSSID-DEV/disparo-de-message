package com.dssiddev.disparowhatsapp.handles;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileHandler {

    public MultipartFile payloadHandle(MultipartFile fileName) throws IOException {
        System.out.println("handler: " + fileName.getOriginalFilename());
        return fileName;
    }
}
