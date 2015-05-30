package com.example.anna.shedule;

/**
 * Created by Anna on 29.05.2015.
 */
public class LessonData {
    int number;
    String lesson;
    String group;
    String type;
    String place;
    boolean hasCencellled;
    boolean hasChanges;


    public LessonData(int number, String lesson, String group, String type, String place) {
        this.number = number;
        this.lesson = lesson;
        this.group = group;
        this.type = type;
        this.place = place;
    }


    public int getNumber() {return number;}
    public String getLesson(){return lesson;}
    public String getGroup() {return group;}
    public String getType(){return type;}
    public String getPlace() {return group;}

}