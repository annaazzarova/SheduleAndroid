package com.example.anna.shedule_v2;

/**
 * Created by Anna on 29.04.2015.
 */
public class DayClass {
    private int m_lessonNumber;
    private String m_name;
    private String m_teacher;
    private String m_place;

    DayClass (int number, String name, String teacher, String place){
        m_name = name;
        m_teacher = teacher;
        m_place = place;
        m_lessonNumber = number;
    }

    public int getM_lessonNumber() {
        return m_lessonNumber;
    }

    public String getM_name() {
        return m_name;
    }

    public String getM_teacher() {
        return m_teacher;
    }

    public String getM_place() {
        return m_place;
    }

    public void setM_lessonNumber(int lessonNumber) {
        this.m_lessonNumber = lessonNumber;
    }

    public void setM_name(String name) {
        this.m_name = name;
    }

    public void setM_teacher(String teacher) {
        this.m_teacher = teacher;
    }

    public void setM_place(String place) {
        this.m_place = place;
    }
}
