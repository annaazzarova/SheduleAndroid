package com.example.anna.shedule.application.schedule.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum WeekPeriodicity {
    RED(1),
    BLUE(2),
    BOTH(3);

    private int id;

    WeekPeriodicity(int id) {
        this.id = id;
    }

    public static WeekPeriodicity getByTypeId(int id) {
        for (WeekPeriodicity period: values()) {
            if (period.id == id) {
                return period;
            }
        }
        return null;
    }

    @JsonCreator
    public static WeekPeriodicity getByType(String type) {
        switch (type) {
            case "red": return RED;
            case "blue": return BLUE;
            case "both": return BOTH;
            default: return null;
        }
    }

}
