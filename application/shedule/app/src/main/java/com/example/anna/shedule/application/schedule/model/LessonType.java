package com.example.anna.shedule.application.schedule.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum LessonType {
    LECTURE("lecture"),
    PRACTICE("practice");

    private String id;

    LessonType(String type) {
        id = type;
    }

    @JsonCreator
    public static LessonType getByTypeId(String str) {
        if (str == null) {
            return null;
        } else if (str.equals("lecture")) {
            return LECTURE;
        } else {
            return PRACTICE;
        }
    }

}
