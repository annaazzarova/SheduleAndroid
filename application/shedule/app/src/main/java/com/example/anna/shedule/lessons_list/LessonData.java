package com.example.anna.shedule.lessons_list;

/**
 * Created by Anna on 29.05.2015.
 */
public class LessonData {
    int number;
    String lesson;
    String group;
    String type;
    String place;
    boolean canceled;
    boolean changed;
    String week;
    String start;
    String end;
    int day_of_week;


    public LessonData(int number, String lesson, String group, String type, String place, String week, int day_of_week) {
        this.number = number;
        this.lesson = lesson;
        this.group = group;
        this.type = type;
        this.place = place;
        this.week = week;
        this.day_of_week = day_of_week;
        if (this.getNumber() == 1) {
            this.start = "8:00";
            this.end = "9:35";
        }
        if (this.getNumber() == 2) {
            this.start = "9:45";
            this.end = "11:20";
        }
        if (this.getNumber() == 3) {
            this.start = "11:30";
            this.end = "13:05";
        }
        if (this.getNumber() == 4) {
            if (day_of_week == 3) {
                this.start = "13:15";
                this.end = "14:00";
            }
            else {
                this.start = "13:30";
                this.end = "15:05";
            }
        }
        if (this.getNumber() == 5) {
            if (day_of_week == 3) {
                this.start = "14:30";
                this.end = "16:05";
            }
            else {
                this.start = "15:15";
                this.end = "16:50";
            }
        }
        if (this.getNumber() == 6) {
            if (day_of_week == 3) {
                this.start = "16:15";
                this.end = "17:50";
            }
            else {
                this.start = "17:00";
                this.end = "18:35";
            }
        }
        if (this.getNumber() == 7) {
            if (day_of_week == 3) {
                this.start = "18:00";
                this.end = "19:35";
            }
            else {
                this.start = "18:45";
                this.end = "20:20";
            }
        }

    }


    public String getStart() {return start;}
    public String getEnd() {return end;}
    public int getNumber() {return number;}
    public String getLesson(){return lesson;}
    public String getGroup() {return group;}
    public String getType(){return type;}
    public String getPlace() {return group;}

    public String getWeek() {return week;}
    public boolean isCanceled() {return canceled;}
    public boolean isChanged() {return changed;}
}