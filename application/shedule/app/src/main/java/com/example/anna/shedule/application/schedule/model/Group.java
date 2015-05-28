package com.example.anna.shedule.application.schedule.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group implements Entity {

    public static final String TABLE_NAME = "groups";

    private long id;
    private String groupId;
    private String group;
    private String faculty;
    private String specialty;
    private String groupCode;
    private String formOfEducation;
    private String course;

    @Override
    public void save(ContentValues values) {
        values.put("groupId", groupId);
        values.put("faculty", faculty);
        values.put("specialty", specialty);
        values.put("groupCode", groupCode);
        values.put("formOfEducation", formOfEducation);
        values.put("course", course);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        groupId = cursor.getString(1);
        group = cursor.getString(2);
        faculty = cursor.getString(3);
        specialty = cursor.getString(4);
        groupCode = cursor.getString(5);
        formOfEducation = cursor.getString(6);
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

        if (groupCode != null) {
            res += sep + "(" + groupCode + ")";
            sep = " ";
        }

        if (formOfEducation != null) {
            res += sep + "(" + formOfEducation + ")";
        }

        return res;
    }

    @Override
    public String getSqlTableFields() {
        return "groupId VARCHAR, group VARCHAR, faculty VARCHAR, " +
                "specialty VARCHAR, groupCode VARCHAR, formOfEducation VARCHAR, course VARCHAR";
    }
}
