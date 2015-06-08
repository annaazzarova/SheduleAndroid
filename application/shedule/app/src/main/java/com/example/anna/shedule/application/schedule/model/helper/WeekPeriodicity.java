package com.example.anna.shedule.application.schedule.model.helper;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

@Getter
public enum WeekPeriodicity {
    RED(1),
    BLUE(2),
    BOTH(3);

    private int id;

    private static final long START_OF_YEAR_2015;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 0, 0);
        START_OF_YEAR_2015 = calendar.getTimeInMillis();
    }

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

    public static WeekPeriodicity getPeriodicity(long timeInMillis) {
        long diff = timeInMillis - START_OF_YEAR_2015;
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        int currentWeek = (int)(days / 7);
        return (currentWeek % 2 == 0) ? BLUE : RED;
    }

}
