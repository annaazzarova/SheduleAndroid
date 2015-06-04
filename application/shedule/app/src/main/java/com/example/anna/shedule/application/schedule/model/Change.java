package com.example.anna.shedule.application.schedule.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.example.anna.shedule.utils.DateUtils;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Change implements Entity {

    public static final String TABLE_NAME = "changes";

    private long id;
    private String changeId;
    private String lessonId;
    private Date dateFrom;
    private Date dateTo;
    private int dayOfWeek;
    private WeekPeriodicity weekPeriodicity;

    @Override
    public void save(ContentValues values) {
        values.put("changeId", changeId);
        values.put("lessonId", lessonId);
        values.put("dayOfWeek", dayOfWeek);
        if (dateTo != null) values.put("dateTo", dateTo.getTime());
        if (dateFrom != null) values.put("dateFrom", dateFrom.getTime());
        if (weekPeriodicity != null) values.put("weekPeriodicity", weekPeriodicity.getId());
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
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "changeId VARCHAR, lessonId VARCHAR, dateFrom INTEGER, dateTo INTEGER, " +
                "dayOfWeek INTEGER, weekPeriodicity INTEGER";
    }
}
