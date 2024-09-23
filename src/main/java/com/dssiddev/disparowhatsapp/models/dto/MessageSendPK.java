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
public class MessageSendPK {
    private String id;
    private boolean fromMe;
    private String remote;
    private String _serialized;

}
