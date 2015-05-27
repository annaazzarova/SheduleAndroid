package com.example.anna.shedule.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.Group;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.utils.ContextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class Database {

    private static final String DB_PATH = "Schedule.db";
    private static Database instance;

    private SQLiteDatabase db;

    public static synchronized Database getDbInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static void dropDatabase() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
        ContextUtils.getContext().deleteDatabase(DB_PATH);
    }

    public void close() {
        instance = null;
        db.close();
        new ContentValues();
    }

    public <T extends Entity> List<T> save(List<T> entities) {
        for (T entity: entities) {
            save(entity);
        }
        return entities;
    }

    public void delete(List<? extends Entity> entities) {
        for (Entity entity: entities) {
            delete(entity);
        }
    }

    public void dropAllElements(String tableName) {
        db.execSQL("DELETE FROM " + tableName);
    }

    @SneakyThrows
    public <T extends Entity> List<T> getAll(Class<T> cs) {
        T entity = cs.newInstance();
        String selectByIdQuery = "SELECT * FROM " + entity.getTableName();
        return getByQuery(cs, selectByIdQuery);
    }

    @SneakyThrows
    public <T extends Entity> T getById(Class<T> cs, long id) {
        T entity = cs.newInstance();
        String selectByIdQuery = "SELECT * FROM " + entity.getTableName() + " WHERE id='" + id + "'";
        Cursor cursor = db.rawQuery(selectByIdQuery, null);

        if (cursor.moveToNext()) {
            entity.load(cursor);
        } else {
            entity = null;
        }

        cursor.close();
        return entity;
    }

    @SneakyThrows
    public <T extends Entity> T getOneByQuery(Class<T> cs, String query) {
        Cursor cursor = db.rawQuery(query, null);
        T entity = cursor.moveToNext() ? mapEntity(cs, cursor) : null;
        cursor.close();
        return entity;
    }

    public <T extends Entity> List<T> getByQuery(Class<T> cs, String query) {
        List<T> entities = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            T entity = mapEntity(cs, cursor);
            entities.add(entity);
        }

        return entities;
    }

    public <T extends Entity> T save(T entity) {
        ContentValues values = new ContentValues();
        entity.save(values);

        long id;
        if (entity.getId() <= 0) {
            id = db.insert(entity.getTableName(), null, values);
            entity.setId(id);
        } else {
            String wereClause = "id " + entity.getId();
            id = db.update(entity.getTableName(), values, wereClause, null);
        }

        if (id == Entity.INVALID_ID) {
            Log.e("db", "Problem with entity " + entity.getClass().getName());
        }

        return entity;
    }


    public <T extends Entity> void delete(T entity) {
        delete(entity.getTableName(), entity.getId());
    }

    public void delete(String tableName, long elementId) {
        db.delete(tableName, "id=" + elementId, null);
    }

    private Database() {
        boolean isExists = isDatabaseExists();
        db = ContextUtils.getContext().openOrCreateDatabase(DB_PATH, Context.MODE_PRIVATE, null);
        if (!isExists) {
            initDatabase();
        }
    }

    @SneakyThrows
    private <T extends Entity> T mapEntity(Class<T> cs, Cursor cursor) {
        T entity = cs.newInstance();
        entity.load(cursor);
        return entity;
    }

    private void initDatabase() {
        createTable(User.class);
        createTable(Group.class);
        createTable(StaticLesson.class);
        createTable(Change.class);
        createTable(Note.class);
    }

    @SneakyThrows
    private void createTable(Class<? extends Entity> cs) {
        Entity entity = cs.newInstance();
        createTable(entity.getTableName(), entity.getSqlTableFields());
    }

    private void createTable(String tableName, String fields) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                "(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + fields + ")");
    }

    private static boolean isDatabaseExists() {
        File dbFile = ContextUtils.getContext().getDatabasePath(DB_PATH);
        return dbFile.exists();
    }

    public int getNumberOfRecordsInTable(String tableName) {
        Cursor cursor = db.rawQuery("SELECT id FROM " + tableName, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
