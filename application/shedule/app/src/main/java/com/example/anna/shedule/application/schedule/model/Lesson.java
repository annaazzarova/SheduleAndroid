package com.example.anna.shedule.application.schedule.model;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("_id")
    private String lessonId;

    @JsonProperty
    private String hull;

    @JsonProperty("lesson")
    private String title;

    @JsonProperty
    private String teacher;

    @JsonProperty
    private String auditory;

    @JsonProperty("day")
    private int dayOfWeek;

    @JsonProperty("number")
    private LessonTime lessonTime;

    @JsonProperty("week")
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
        values.put("lessonId", lessonId);
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
        lessonId = cursor.getString(8);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "title VARCHAR, hull VARCHAR, auditory VARCHAR, dayOfWeek INTEGER, " +
                "teacher VARCHAR, weekPeriodicity INTEGER, lessonTime INTEGER, lessonId VARCHAR";
    }
}
