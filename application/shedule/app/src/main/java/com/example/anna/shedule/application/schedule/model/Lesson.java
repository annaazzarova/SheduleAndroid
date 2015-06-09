package com.example.anna.shedule.application.schedule.model;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.LessonTime;
import com.example.anna.shedule.application.schedule.model.helper.LessonType;
import com.example.anna.shedule.application.schedule.model.helper.StaticLesson;
import com.example.anna.shedule.application.schedule.model.helper.WeekPeriodicity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Lesson implements Serializable {
    private StaticLesson lesson;
    private Change change;

    @Getter
    @Setter
    private List<Note> notes = Collections.emptyList();

    @Getter
    @Setter
    private List<Group> groups = Collections.emptyList();

    @Getter
    @Setter
    private long startOfLessonDay;

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

    public int getDayOfWeek() {
        int dayOfWeek = 0;
        if (change != null) {
            dayOfWeek = change.getDayOfWeek();
        } else if (lesson != null) {
            dayOfWeek = lesson.getDayOfWeek();
        }
        return dayOfWeek;
    }

    public WeekPeriodicity getWeekPeriodicity() {
        WeekPeriodicity weekPeriodicity = null;
        if (change != null && change.getWeekPeriodicity() != null) {
            weekPeriodicity = change.getWeekPeriodicity();
        } else if (lesson != null) {
            weekPeriodicity = lesson.getWeekPeriodicity();
        }
        return weekPeriodicity;
    }

    public LessonType getType() {
        LessonType type = null;
        if (change != null && change.getType() != null) {
            type = change.getType();
        } else if (lesson != null) {
            type = lesson.getType();
        }
        return type;
    }

    public LessonStatus getStatus() {
        LessonStatus status = LessonStatus.NORMAL;
        if (change != null && change.getStatus() != null) {
            status = change.getStatus();
        }
        return status;
    }

    public LessonTime getTime() {
        LessonTime time = null;
        if (change != null && change.getTime() != null) {
            time = change.getTime();
        } else if (lesson != null) {
            time = lesson.getLessonTime();
        }
        return time;
    }

    public String getTitle() {
        String title = null;
        if (change != null && change.getTitle() != null) {
            title = change.getTitle();
        } else if (lesson != null) {
            title = lesson.getTitle();
        }
        return title;
    }

    public String getHull() {
        String hull = null;
        if (change != null && change.getHull() != null) {
            hull = change.getHull();
        } else if (lesson != null) {
            hull = lesson.getHull();
        }
        return hull;
    }

    public String getAuditory() {
        String auditory = null;
        if (change != null && change.getAuditory() != null) {
            auditory = change.getAuditory();
        } else if (lesson != null) {
            auditory = lesson.getAuditory();
        }
        return auditory;
    }

    public String getTeacherId() {
        String teacherId = null;
        if (change != null && change.getTeacherId() != null) {
            teacherId = change.getTeacherId();
        } else if (lesson != null) {
            teacherId = lesson.getTeacherId();
        }
        return teacherId;
    }

    public String getTeacherName() {
        String teacherName = null;
        if (change != null && change.getTeacherName() != null) {
            teacherName = change.getTeacherName();
        } else if (lesson != null) {
            teacherName = lesson.getTeacherName();
        }
        return teacherName;
    }

    public boolean isChangedLesson() {
        return change != null;
    }

    public String getLessonId() {
        return (lesson != null) ? lesson.getLessonId() : null;
    }

    public String getChangeId() {
        return (change != null) ? change.getChangeId() : null;
    }

    public boolean hasNotes() {
        return !notes.isEmpty();
    }

    public String getGroupsAsString() {

        return null;
    }
}
