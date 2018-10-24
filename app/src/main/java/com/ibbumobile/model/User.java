package com.ibbumobile.model;

public class User {

      final String name ;
      final String matric ;
      final String department ;
      final String gender ;
      final String level ;

    public User(String name, String matric, String department, String gender, String level) {
        this.name = name;
        this.matric = matric;
        this.department = department;
        this.gender = gender;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getMatric() {
        return matric;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public String getLevel() {
        return level;
    }
}
