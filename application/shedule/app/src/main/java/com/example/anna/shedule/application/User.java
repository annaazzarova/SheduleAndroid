package com.example.anna.shedule.application;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Entity {

    private long id;
    private String name;

    @Override
    public void save(ContentValues values) {
        values.put("name", name);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        name = cursor.getString(1);
    }

    @Override
    public String getTableName() {
        return "user";
    }
}
