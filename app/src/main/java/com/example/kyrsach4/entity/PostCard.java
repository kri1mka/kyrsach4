package com.example.kyrsach4.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostCard {

    private Integer id;

    private Integer userId;

    private String description;
    private String location;

    private String createdAt;

    private String photoIt;

    private String userName; // имя пользователя
    private Integer likesCount;


    private boolean liked = false;

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    private String photo;

    public String getPhoto() {
        return photo;
    }

    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPhotoIt() { return photoIt; }
    public void setPhotoIt(String photoIt) { this.photoIt = photoIt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getLikesCount() { return likesCount; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }
}
