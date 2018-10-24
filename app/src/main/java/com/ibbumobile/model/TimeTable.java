package com.ibbumobile.model;

import java.io.Serializable;

/**
 * Created by Eidris on 3/16/2018.
 */

public class TimeTable implements Serializable {

    private final String courseCode;
    private final String day;
    private final int time;
    private final int per;
    private final String teacherName;
    private final String room;

    public TimeTable(String courseCode, String day, int time, int per, String teacherName, String room) {
        this.courseCode = courseCode;
        this.day = day;
        this.time = time;
        this.per = per;
        this.teacherName = teacherName;
        this.room = room;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getPer() {
        return per;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getRoom() {
        return room;
    }
}