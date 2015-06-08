package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.schedule.model.Teacher;
import com.example.anna.shedule.application.schedule.model.helper.StaticLesson;
import com.example.anna.shedule.application.services.Services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeacherService {

    private final StaticLessonsService staticLessonsService;

    private Set<Teacher> teachers;

    public TeacherService() {
        this.staticLessonsService = Services.getService(StaticLessonsService.class);
    }

    public Set<Teacher> getAllTeachers() {
        if (teachers == null) {
            teachers = getTeachersFromLesson();
        }
        return teachers;
    }

    private Set<Teacher> getTeachersFromLesson() {
        List<StaticLesson> lessons = staticLessonsService.getAllLessons();
        Set<Teacher> teachers = new HashSet<>();
        for (StaticLesson lesson : lessons) {
            Teacher teacher = new Teacher(lesson.getTeacherId(), lesson.getTeacherName());
            teachers.add(teacher);
        }
        return teachers;
    }
}
