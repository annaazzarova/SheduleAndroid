package com.example.anna.shedule.application;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Entity {

    public static final String TABLE_NAME = "user";

    private long id;
    private String name;
    private UserType type;
    // teacherId classLeaderId or groupId
    private String extendedId;

    @Override
    public void save(ContentValues values) {
        values.put("name", name);
        if (type != null) values.put("userType", type.getId());
        values.put("extendedId", extendedId);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        name = cursor.getString(1);
        type = UserType.getByTypeId(cursor.getInt(2));
        extendedId = cursor.getString(3);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "name VARCHAR, userType INTEGER, extendedId VARCHAR";
    }

    public void setGroupId(String groupId) {
        this.extendedId = groupId;
        this.type = UserType.STUDENT;
    }
}
