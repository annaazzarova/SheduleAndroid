package com.example.anna.shedule.application.user.model;

import lombok.Getter;

@Getter
public enum UserType {
    TEACHER(1),
    STUDENT(2),
    CLASS_LEADER(3);

    private int id;

    UserType(int id) {
        this.id = id;
    }


    public static UserType getByTypeId(int id) {
        for (UserType userType: values()) {
            if (userType.id == id) {
                return userType;
            }
        }
        return null;
    }
}
