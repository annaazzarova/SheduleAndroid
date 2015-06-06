package com.example.anna.shedule.application.schedule.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Lesson {
    private StaticLesson lesson;
    private Change change;
    private List<Group> groups;

    public Lesson(StaticLesson lesson, Change change) {
        this.lesson = lesson;
        this.change = change;
    }

    public Lesson(Change change) {
        this(null, change);
    }

    public String getId() {
        String id = "";
        if (change != null) {
            id = change.getChangeId();
        } else if (lesson != null) {
            id = lesson.getLessonId();
        }
        return id;
    }

    public String getGroupIds() {
        String groups = null;
        if (change != null && change.getGroupIds() != null) {
            groups = change.getGroupIds();
        } else if (lesson != null) {
            groups = lesson.getGroupIds();
        }
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
