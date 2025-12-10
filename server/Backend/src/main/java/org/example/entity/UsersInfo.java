package org.example.entity;

import java.util.Date;

public class UsersInfo {
    private Integer id;
    private Integer user_id;
    private Date date_of_birth;
    private String sex;
    private Integer age;
    private String interests;
    private String about;

    public UsersInfo() {}

    public UsersInfo(Integer user_id, Date date_of_birth, String sex, Integer age, String interests, String about) {
        this.user_id = user_id;
        this.date_of_birth = date_of_birth;
        this.sex = sex;
        this.age = age;
        this.interests = interests;
        this.about = about;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
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
