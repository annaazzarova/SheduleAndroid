package com.example.anna.shedule_v2;

public class DataObject {
    private String m_name;
    private String m_teacher;
    private String m_place;
    private int m_number;


    DataObject (String text1, String text2, String text3, int text4){
        m_name = text1;
        m_teacher = text2;
        m_place = text3;
        m_number = text4;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_teacher() {
        return m_teacher;
    }

    public void setM_teacher(String m_teacher) {
        this.m_teacher = m_teacher;
    }

    public String getM_place() {
        return m_place;
    }

    public void setM_place(String m_place) {
        this.m_place = m_place;
    }

    public String getM_date_start_time() {
        switch(m_number) {
            case 1: return "8:00";
            case 2: return "9:45";
            case 3: return "11:30";
            case 4: return "13:30";
            case 5: return "15:15";
            case 6: return "17:00";
            default: return "8:00";
        }
    }

    public String getM_date_end_time() {
        switch(m_number) {
            case 1: return "9:35";
            case 2: return "11:20";
            case 3: return "13:05";
            case 4: return "15:05";
            case 5: return "16:50";
            case 6: return "18:35";
            default: return "9:35";
        }
    }

    public void setM_date_number(int m_number) {
        this.m_number = m_number;
    }


}