package org.example.entity;

import java.util.Date;

public class UsersInfo {
    private int id;
    private int userId;
    private int age;
    private Date dateOfBirth;
    private String sex;
    private String about;
    private String interests;

    public UsersInfo() {}

    public UsersInfo(int id, int user_id, Date date_of_birth, String sex, int age, String interests, String about) {
        this.id = id;
        this.userId = user_id;
        this.sex = sex;
        this.age = age;
        this.dateOfBirth = date_of_birth;
        this.about = about;
        this.interests = interests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer user_id) {
        this.userId = user_id;
    }

    public java.sql.Date getDateOfBirth() {
        return (java.sql.Date) dateOfBirth;
    }

    public void setDateOfBirth(Date date_of_birth) {
        this.dateOfBirth = date_of_birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
