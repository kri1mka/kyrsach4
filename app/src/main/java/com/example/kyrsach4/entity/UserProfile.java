package com.example.kyrsach4.entity;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    private Integer id;
    private String name;
    private String surname;
    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    private Integer age;
    private String location;
    private String travelType;
    private String avatarUrl;
    private String interests;
    private String about;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getFullName() { return name + " " + surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTravelType() { return travelType; }
    public void setTravelType(String travelType) { this.travelType = travelType; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public String getPhoto() {
        return avatarUrl;
    }

    public void setPhoto(String photo) {
        this.avatarUrl = photo;
    }
}
