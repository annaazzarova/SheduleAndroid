package com.example.anna.shedule.application.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum UserType {
    TEACHER("teacher"),
    STUDENT("student"),
    CLASS_LEADER("classLeader");

    private String id;

    UserType(String id) {
        this.id = id;
    }


    @JsonCreator
    public static UserType getByTypeId(String id) {
        for (UserType userType: values()) {
            if (userType.id.equals(id)) {
                return userType;
            }
        }
        return null;
    }
}
