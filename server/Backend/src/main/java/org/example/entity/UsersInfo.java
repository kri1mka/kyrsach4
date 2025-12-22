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
    private String city;
    private String travelType;
    private String avatarUrl;

    public UsersInfo() {}

    public UsersInfo(int id, int userId, Date dateOfBirth, String sex, int age, String interests, String about) {
        this.id = id;
        this.userId = userId;
        this.sex = sex;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public java.sql.Date getDateOfBirth() {
        return (java.sql.Date) dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getAvatarUrl() {
        return  avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this. avatarUrl = avatarUrl;
    }
}
