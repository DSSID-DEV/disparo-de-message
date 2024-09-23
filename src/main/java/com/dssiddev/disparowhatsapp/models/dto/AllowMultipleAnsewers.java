package com.dssiddev.disparowhatsapp.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllowMultipleAnsewers {
    private boolean allowMultipleAnswers;

    public String toString() {
        return "\"allowMultipleAnswers\": " + allowMultipleAnswers;
    }
}
