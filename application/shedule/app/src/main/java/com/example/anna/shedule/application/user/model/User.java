package com.example.anna.shedule.application.user.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.anna.shedule.application.database.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Entity {

    public static final String TABLE_NAME = "users";

    private long id;

    @JsonProperty("_id")
    private String userId;

    @JsonProperty
    private String name;

    @JsonProperty
    private String username;

    @JsonProperty
    private UserType type;

    @JsonProperty
    private String groupId;

    @Override
    public void save(ContentValues values) {
        values.put("name", name);
        if (type != null) values.put("userType", type.getId());
        values.put("groupId", groupId);
        values.put("username", username);
        values.put("userId", userId);
    }

    @Override
    public void load(Cursor cursor) {
        id = cursor.getLong(0);
        name = cursor.getString(1);
        type = UserType.getByTypeId(cursor.getString(2));
        groupId = cursor.getString(3);
        username = cursor.getString(4);
        userId = cursor.getString(5);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getSqlTableFields() {
        return "name VARCHAR, userType VARCHAR, groupId VARCHAR, username VARCHAR, userId VARCHAR";
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        this.type = UserType.STUDENT;
    }
}
