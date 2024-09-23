package com.dssiddev.disparowhatsapp.models.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentType {
    STRING("string"),
    MESSAGE_MEDIA("MessageMedia"),
    MESSAGE_MEDIA_FROM_URL("MessageMediaFromURL"),
    POLL("Poll");

    private String type;

    public static ContentType from(String type) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getType().equals(type)) {
                return contentType;
            }
        }
        return null;
    }

}
