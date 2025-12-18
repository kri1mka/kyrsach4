package org.example.entity;

import java.util.Date;

public class PostCard {
    private Integer id;
    private Integer userId;
    private String description;
    private String location;
    private Date createdAt;
    private String photoIt;

    public PostCard() {}

    public PostCard(Integer user_id, String description, String location, String photo_it) {
        this.userId = user_id;
        this.description = description;
        this.location = location;
        this.photoIt = photo_it;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = created_at;
    }

    public String getPhotoIt() {
        return photoIt;
    }

    public void setPhotoIt(String photo_it) {
        this.photoIt = photo_it;
    }


}
