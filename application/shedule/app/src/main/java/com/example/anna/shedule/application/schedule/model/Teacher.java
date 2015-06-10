package com.example.anna.shedule.application.schedule.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Teacher implements Entity, Serializable {

    public static final String TABLE_NAME = "teacher";

    private long id;

    @JsonProperty("_id")
    private String teacherId;

    @JsonProperty
    private String name;

    public Teacher(String teacherId, String teacherName) {
        this.name = teacherName;
        this.teacherId = teacherId;
    }

    @Override
    public void save(ContentValues values) {
        values.put("teacherId", teacherId);
        values.put("name", name);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        teacherId = cursor.getString(1);
        name = cursor.getString(2);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "teacherId VARCHAR, name VARCHAR";
    }
}
