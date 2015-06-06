package com.example.anna.shedule.application.schedule.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Lesson {
    private StaticLesson lesson;
    private Change change;

    public Lesson(StaticLesson lesson, Change change) {
        this.lesson = lesson;
        this.change = change;
    }

    public Lesson(Change change) {
        this(null, change);
    }
}
