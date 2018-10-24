package com.ibbumobile.model;

public class Student {

    final String firstName;
    final String lastName;
    final String otherName;
    final String matNum;
    final String dept;
    final String gender;
    final String email;
    final String level;

    public Student(String firstName, String lastName, String otherName, String matNum, String dept, String gender, String email, String level) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.matNum = matNum;
        this.dept = dept;
        this.gender = gender;
        this.email = email;
        this.level = level;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOtherName() {
        return otherName;
    }

    public String getMatNum() {
        return matNum;
    }

    public String getDept() {
        return dept;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getLevel() {
        return level;
    }
}
