package com.ibbumobile.model;

public class Staff {

    final String firstName;
    final String lastName;
    final String otherName;
    final String staffID;
    final String dept;
    final String stafType;
    final String gender;
    final String email;

    public Staff (String firstName, String lastName, String otherName, String staffID, String dept, String stafType, String gender, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.staffID = staffID;
        this.dept = dept;
        this.stafType = stafType;
        this.gender = gender;
        this.email = email;

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

    public String getStaffID() {
        return staffID;
    }

    public String getDept() {
        return dept;
    }

    public String getStafType() {
        return stafType;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }
}
