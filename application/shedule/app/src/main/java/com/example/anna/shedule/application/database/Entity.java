package com.example.anna.shedule.application.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

public interface Entity extends Serializable {

    public static final long INVALID_ID = -1;

    long getId();

    void setId(long id);

    void save(ContentValues values);

    void load(Cursor cursor);

    String getTableName();

    String getSqlTableFields();
}
