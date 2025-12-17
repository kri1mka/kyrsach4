package com.example.kyrsach4.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TripCard {

    private Integer id;

    @SerializedName("user_id")
    private Integer userId;

    private String location;

    private String startDate;

    private String endDate;

    private Double price;
    private String type;
    private String description;

    private String createdAt;

    private String photoIt;

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPhotoIt() { return photoIt; }
    public void setPhotoIt(String photoIt) { this.photoIt = photoIt; }
}
