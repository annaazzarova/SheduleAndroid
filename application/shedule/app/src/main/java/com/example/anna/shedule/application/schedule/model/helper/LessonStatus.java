package com.example.anna.shedule.application.schedule.model.helper;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum LessonStatus {
    NORMAL("normal"),
    CANCELED("canceled");

    private String id;

    LessonStatus(String type) {
        id = type;
    }

    @JsonCreator
    public static LessonStatus getByTypeId(String str) {
        if (str == null) {
            return null;
        } else if (str.equals("normal")) {
            return NORMAL;
        } else {
            return CANCELED;
        }
    }
}
