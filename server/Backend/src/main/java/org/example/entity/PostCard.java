package org.example.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostCard {
    private Integer id;
    private Integer userId;
    private String description;
    private String location;
    private Date createdAt;
    private String photoIt;

    // Новое поле для полного имени пользователя
    private String userName;

    private Integer likesCount;


    // Геттеры и сеттеры
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getPhotoIt() { return photoIt; }
    public void setPhotoIt(String photoIt) { this.photoIt = photoIt; }

    // Геттер и сеттер для полного имени пользователя
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getLikesCount() { return likesCount; }
    public void setLikesCount(Integer likesCount) { this.likesCount = likesCount; }

}
