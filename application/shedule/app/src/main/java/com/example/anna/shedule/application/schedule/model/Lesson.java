package com.example.anna.shedule.application.schedule.model;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Lesson implements Entity {

    public static final String TABLE_NAME = "lessons";

    private long id;
    private String hull;
    private String title;
    private String teacher;
    private String auditory;
    private int dayOfWeek;
    private LessonTime lessonTime;
    private WeekPeriodicity weekPeriodicity;
    private String groupIds;

    private transient List<Group> groups;


    @Override
    public void save(ContentValues values) {
        values.put("hull", hull);
        values.put("title", title);
        values.put("teacher", teacher);
        values.put("auditory", auditory);
        values.put("dayOfWeek", dayOfWeek);
        if (lessonTime != null) values.put("lessonTime", lessonTime.getId());
        if (weekPeriodicity != null) values.put("weekPeriodicity", weekPeriodicity.getId());
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        title = cursor.getString(1);
        hull = cursor.getString(2);
        auditory = cursor.getString(3);
        dayOfWeek = cursor.getInt(4);
        teacher = cursor.getString(5);
        weekPeriodicity = WeekPeriodicity.getByTypeId(cursor.getInt(6));
        lessonTime = LessonTime.getByTypeId(cursor.getInt(7));
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "title VARCHAR, hull VARCHAR, auditory VARCHAR, dayOfWeek INTEGER, " +
                "teacher VARCHAR, weekPeriodicity INTEGER, lessonTime INTEGER";
    }
}
