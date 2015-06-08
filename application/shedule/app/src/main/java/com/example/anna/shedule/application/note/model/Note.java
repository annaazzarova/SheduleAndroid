package com.example.anna.shedule.application.note.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.example.anna.shedule.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Note implements Entity {

    public static final String TABLE_NAME = "notes";

    private long id;

    @JsonProperty
    private String text;

    @JsonProperty("_id")
    private String noteId;

    @JsonProperty
    private String lessonId;

    @JsonProperty
    private String changeId;

    @JsonProperty
    private String ownerId;

    @JsonProperty
    private String ownerType;

    @JsonProperty
    private String ownerExtraData;

    @JsonProperty
    private Date dateCreate;

    @JsonProperty
    private Date date;

    @Override
    public void save(ContentValues values) {
        values.put("text", text);
        values.put("noteId", noteId);
        values.put("lessonId", lessonId);
        values.put("changeId", changeId);
        values.put("ownerId", ownerId);
        values.put("ownerType", ownerType);
        values.put("ownerExtraData", ownerExtraData);
        if (dateCreate != null) values.put("dateCreate", dateCreate.getTime());
        if (date != null) values.put("date", date.getTime());
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        text = cursor.getString(1);
        noteId = cursor.getString(2);
        lessonId = cursor.getString(3);
        changeId = cursor.getString(4);
        ownerId = cursor.getString(5);
        ownerType = cursor.getString(6);
        ownerExtraData = cursor.getString(7);
        dateCreate = DateUtils.toDate(cursor, 8);
        date = DateUtils.toDate(cursor, 9);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "text TEXT, noteId VARCHAR, lessonId VARCHAR, changeId VARCHAR, ownerId VARCHAR, " +
                "ownerType VARCHAR, ownerExtraData VARCHAR, dateCreate INTEGER, date INTEGER";
    }
}
