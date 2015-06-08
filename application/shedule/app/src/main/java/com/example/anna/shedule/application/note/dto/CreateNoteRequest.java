package com.example.anna.shedule.application.note.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateNoteRequest {

    private String text;
    private Date date;

    public CreateNoteRequest(String text, long date) {
        this.text = text;
        this.date = new Date(date);
    }
}
