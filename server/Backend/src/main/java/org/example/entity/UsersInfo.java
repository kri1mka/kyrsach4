package org.example.entity;


import java.util.Date;


public class UsersInfo {
    private Integer id;
    private Integer userId;
    private Date dateOfBirth;
    private String sex;
    private Integer age;
    private String interests;
    private String about;
    private String city;
    private String travelType;
    private String  avatarUrl;


    public UsersInfo() {}


    public UsersInfo(Integer userId, Date dateOfBirth, String sex, Integer age, String interests, String about, String city, String travelType,String avatarUrl) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.age = age;
        this.interests = interests;
        this.about = about;
        this.city = city;
        this.travelType = travelType;
        this. avatarUrl =  avatarUrl;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }


    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }


    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }


    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }


    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }


    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }


    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

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