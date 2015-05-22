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
    private String title;

    @Override
    public void save(ContentValues values) {
        values.put("groupId", groupId);
        values.put("title", title);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        groupId = cursor.getString(1);
        title = cursor.getString(2);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "groupId VARCHAR, title VARCHAR";
    }
}
