package com.ibbumobile;

public class Score {

    private String course;
    private int load;
    private String grade;

    public Score(String course, int load, String grade) {
        this.course = course;
        this.load = load;
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
