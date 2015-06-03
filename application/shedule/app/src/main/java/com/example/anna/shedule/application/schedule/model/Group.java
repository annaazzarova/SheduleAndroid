package com.example.anna.shedule.application.schedule.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group implements Entity {

    public static final String TABLE_NAME = "groups";

    private long id;

    @JsonProperty("_id")
    private String groupId;

    @JsonProperty
    private String group;

    @JsonProperty
    private String faculty;

    @JsonProperty
    private String specialty;

    @JsonProperty
    private String code;

    @JsonProperty
    private String educationFrom;

    @JsonProperty
    private String course;

    @Override
    public void save(ContentValues values) {
        values.put("groupId", groupId);
        values.put("faculty", faculty);
        values.put("specialty", specialty);
        values.put("code", code);
        values.put("educationFrom", educationFrom);
        values.put("course", course);
        values.put("groupNumber", group);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        groupId = cursor.getString(1);
        group = cursor.getString(2);
        faculty = cursor.getString(3);
        specialty = cursor.getString(4);
        code = cursor.getString(5);
        educationFrom = cursor.getString(6);
        course = cursor.getString(7);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String toString() {
        String res = "";
        String sep = "";
        if (group != null) {
            res += group;
            sep = " ";
        }

        if (code != null) {
            res += sep + "(" + code + ")";
            sep = " ";
        }

        if (educationFrom != null) {
            res += sep + "(" + educationFrom + ")";
        }

        return res;
    }

    @Override
    public String getSqlTableFields() {
        return " groupId VARCHAR, groupNumber VARCHAR, faculty VARCHAR, " +
                "specialty VARCHAR, code VARCHAR, educationFrom VARCHAR, course VARCHAR";
    }
}
