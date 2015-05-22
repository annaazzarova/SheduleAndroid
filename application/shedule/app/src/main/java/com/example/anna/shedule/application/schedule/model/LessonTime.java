package com.example.anna.shedule.application.schedule.model;

import lombok.Getter;

@Getter
public enum LessonTime {
    T_0800_0935(1, "08:00", "09:35"),
    T_0945_0935(2, "09:45", "11:20"),
    T_1130_1305(3, "11:30", "13:05"),
    T_1330_1505(4, "13:30", "15:05"),
    T_1515_1650(5, "15:15", "16:50"),
    T_1700_1835(6, "17:00", "18:35"),
    T_1845_2020(7, "18:45", "20:20");

    private int id;
    private String startTime;
    private String endTime;

    LessonTime(int id, String start, String end) {
        this.id = id;
        this.startTime = start;
        this.endTime = end;
    }

    public static LessonTime getByTypeId(int id) {
        for (LessonTime time: values()) {
            if (time.id == id) {
                return time;
            }
        }
        return null;
    }
}
