package com.example.anna.shedule.application.schedule.model;


import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.example.anna.shedule.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticLesson implements Entity {

    public static final String TABLE_NAME = "lessons";

    private long id;

    @JsonProperty("_id")
    private String lessonId;

    @JsonProperty
    private String hull;

    @JsonProperty
    private String title;

    @JsonProperty
    private String teacherId;

    @JsonProperty
    private String teacherName;

    @JsonProperty
    private String auditory;

    @JsonProperty
    private int dayOfWeek;

    @JsonProperty("time")
    private LessonTime lessonTime;

    @JsonProperty("week")
    private WeekPeriodicity weekPeriodicity;

    @JsonProperty
    private LessonType type;

    private String groupIds;

    private transient List<Group> groups;

    @JsonSetter("groups")
    private void groupJsonSetter(List<String> strings) {
        groupIds = StringUtils.join(strings, ",");
    }

    @Override
    public void save(ContentValues values) {
        values.put("hull", hull);
        values.put("title", title);
        values.put("teacherId", teacherId);
        values.put("teacherName", teacherName);
        values.put("auditory", auditory);
        values.put("dayOfWeek", dayOfWeek);
        values.put("lessonId", lessonId);
        if (lessonTime != null) values.put("lessonTime", lessonTime.getId());
        if (weekPeriodicity != null) values.put("weekPeriodicity", weekPeriodicity.getId());
        if (type != null) values.put("type", type.getId());
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        title = cursor.getString(1);
        hull = cursor.getString(2);
        auditory = cursor.getString(3);
        dayOfWeek = cursor.getInt(4);
        teacherId = cursor.getString(5);
        weekPeriodicity = WeekPeriodicity.getByTypeId(cursor.getInt(6));
        lessonTime = LessonTime.getByTypeId(cursor.getInt(7));
        lessonId = cursor.getString(8);
        teacherName = cursor.getString(9);
        type = LessonType.getByTypeId(cursor.getString(10));
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "title VARCHAR, hull VARCHAR, auditory VARCHAR, dayOfWeek INTEGER, " +
                "teacherId VARCHAR, weekPeriodicity INTEGER, lessonTime INTEGER, " +
                "lessonId VARCHAR, teacherName VARCHAR type VARCHAR";
    }
}
