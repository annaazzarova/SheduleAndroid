package com.example.anna.shedule.application.schedule.model;

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

}
