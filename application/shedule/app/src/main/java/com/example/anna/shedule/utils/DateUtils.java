package com.example.anna.shedule.utils;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final Calendar CALENDAR = Calendar.getInstance();

    public static final long DAY = TimeUnit.DAYS.toMillis(1);
    public static final long HALF_DAY = DAY / 2;

    public static Date toDate(Cursor cursor, int pos) {
        long timeInMs = cursor.getLong(pos);
        return (timeInMs == 0) ? null : new Date(timeInMs);
    }

    public static long middleOfDay(int year, int month, int day) {
        synchronized (CALENDAR) {
            CALENDAR.set(year, month, day);
            long startDayTime = CALENDAR.getTimeInMillis();
            return startDayTime + HALF_DAY;
        }
    }

    public static int dayOfWeek(int year, int month, int day) {
        synchronized (CALENDAR) {
            CALENDAR.set(year, month, day);
            return (CALENDAR.get(Calendar.DAY_OF_WEEK) + 6) % 7;
        }
    }

    public static long startOfDay(int year, int month, int day) {
        synchronized (CALENDAR) {
            CALENDAR.set(year, month, day);
            long startDayTime = CALENDAR.getTimeInMillis();
            return startDayTime;
        }
    }
}
