package com.example.anna.shedule.application.note.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Note implements Entity {

    public static final String TABLE_NAME = "notes";

    private long id;
    private String text;
    private String noteId;
    private String lessonId;

    @Override
    public void save(ContentValues values) {
        values.put("text", text);
        values.put("noteId", noteId);
        values.put("lessonId", lessonId);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(1);
        noteId = cursor.getString(1);
        text = cursor.getString(2);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "noteId VARCHAR, text TEXT";
    }
}
