package com.example.anna.shedule.application.schedule.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.LessonTime;
import com.example.anna.shedule.application.schedule.model.helper.LessonType;
import com.example.anna.shedule.application.schedule.model.helper.WeekPeriodicity;
import com.example.anna.shedule.utils.DateUtils;
import com.example.anna.shedule.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Change implements Entity {

    public static final String TABLE_NAME = "changes";

    private long id;

    @JsonProperty("_id")
    private String changeId;

    @JsonProperty
    private String lessonId;

    @JsonProperty("durationFrom")
    private Date dateFrom;

    @JsonProperty("durationTo")
    private Date dateTo;

    @JsonProperty
    private int dayOfWeek;

    @JsonProperty("week")
    private WeekPeriodicity weekPeriodicity;

    @JsonProperty
    private LessonType type;

    @JsonProperty
    private LessonStatus status;

    @JsonProperty
    private LessonTime time;

    @JsonProperty
    private String title;

    @JsonProperty
    private String hull;

    @JsonProperty
    private String auditory;

    @JsonProperty
    private String teacherId;

    @JsonProperty
    private String teacherName;

    private String groupIds;

    @JsonSetter("groups")
    private void groupJsonSetter(List<String> strings) {
        groupIds = StringUtils.join(strings, ",");
    }

    @Override
    public void save(ContentValues values) {
        values.put("changeId", changeId);
        values.put("lessonId", lessonId);
        if (dateTo != null) values.put("dateTo", dateTo.getTime());
        if (dateFrom != null) values.put("dateFrom", dateFrom.getTime());
        values.put("dayOfWeek", dayOfWeek);
        if (weekPeriodicity != null) values.put("weekPeriodicity", weekPeriodicity.getId());
        if (type != null) values.put("type", type.getId());
        if (status != null) values.put("status", status.getId());
        if (time != null) values.put("time", time.getId());
        values.put("title", title);
        values.put("hull", hull);
        values.put("auditory", auditory);
        values.put("teacherId", teacherId);
        values.put("teacherName", teacherName);
        values.put("groupIds", groupIds);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        changeId = cursor.getString(1);
        lessonId = cursor.getString(2);
        dateFrom = DateUtils.toDate(cursor, 3);
        dateTo = DateUtils.toDate(cursor, 4);
        dayOfWeek = cursor.getInt(5);
        weekPeriodicity = WeekPeriodicity.getByTypeId(cursor.getInt(6));
        type = LessonType.getByTypeId(cursor.getString(7));
        status = LessonStatus.getByTypeId(cursor.getString(8));
        time = LessonTime.getByTypeId(cursor.getInt(9));
        title = cursor.getString(10);
        hull = cursor.getString(11);
        auditory = cursor.getString(12);
        teacherId = cursor.getString(13);
        teacherName = cursor.getString(14);
        groupIds = cursor.getString(15);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "changeId VARCHAR, lessonId VARCHAR, dateFrom INTEGER, dateTo INTEGER, " +
                "dayOfWeek INTEGER, weekPeriodicity INTEGER, type VARCHAR, status VARCHAR, " +
                "time INTEGER, title VARCHAR, hull VARCHAR, auditory VARCHAR, teacherId VARCHAR, " +
                "teacherName VARCHAR, groupIds VARCHAR";
    }
}
