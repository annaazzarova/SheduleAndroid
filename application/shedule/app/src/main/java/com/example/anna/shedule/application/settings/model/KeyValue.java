package com.example.anna.shedule.application.settings.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyValue implements Entity {

    public static final String TABLE_NAME = "settings";

    private long id;
    private String key;
    private String value;

    @Override
    public void save(ContentValues values) {
        values.put("key", key);
        values.put("value", value);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        key = cursor.getString(1);
        value = cursor.getString(2);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "key VARCHAR, value VARCHAR";
    }
}
