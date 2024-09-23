package com.dssiddev.disparowhatsapp.models.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDTO {

    private MessageSendPK id;
    private String body;
    private String type;
    private String from;
    private String to;
}
