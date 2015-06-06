package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleService {

    private StaticLessonsService lessonsService;
    private LessonsChangesService changesService;

    public ScheduleService() {
        lessonsService = Services.getService(StaticLessonsService.class);
        changesService = Services.getService(LessonsChangesService.class);
    }

    public List<Lesson> getSchedule(int year, int month, int day) {
        long time = DateUtils.middleOfDay(year, month, day);
        int dayOfWeek = DateUtils.dayOfWeek(year, month, day);
        WeekPeriodicity periodicity = WeekPeriodicity.getPeriodicity(time);
        List<StaticLesson> lessons = lessonsService.getLessons(periodicity, dayOfWeek);
        List<Change> changes = changesService.getChanges(year, month, day);
        return merge(lessons, changes, dayOfWeek, periodicity);
    }

    public boolean update() {
        return lessonsService.updateLessons() && changesService.update();
    }

    private List<Lesson> merge(List<StaticLesson> staticLessons, List<Change> changes, int dayOfWeek, WeekPeriodicity periodicity) {
        Map<String, Change> lessonIdToChange = new HashMap<>();
        List<Change> newLessons = new ArrayList<>();
        for (Change change: changes) {
            String lessonId = change.getLessonId();
            if (lessonId != null) {
                lessonIdToChange.put(lessonId, change);
            } else {
                newLessons.add(change);
            }
        }

        List<Lesson> lessons = new ArrayList<>();
        for (StaticLesson staticLesson: staticLessons) {
            Change change = lessonIdToChange.get(staticLesson.getLessonId());
            Lesson lesson = new Lesson(staticLesson, change);
            lessons.add(lesson);
        }

        for (Change change: newLessons) {
            WeekPeriodicity changePeriodicity = change.getWeekPeriodicity();
            if (change.getDayOfWeek() == dayOfWeek &&
                    (changePeriodicity == WeekPeriodicity.BLUE
                            || changePeriodicity == WeekPeriodicity.BOTH)) {
                Lesson lesson = new Lesson(change);
                lessons.add(lesson);
            }
        }

        return lessons;
    }

}
