package org.example.entity;

import java.util.Date;

public class PostCard {
    private Integer id;
    private Integer user_id;
    private String description;
    private String location;
    private Date created_at;
    private String photo_it;

    public PostCard() {}

    public PostCard(Integer user_id, String description, String location, String photo_it) {
        this.user_id = user_id;
        this.description = description;
        this.location = location;
        this.photo_it = photo_it;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPhoto_it() {
        return photo_it;
    }

    public void setPhoto_it(String photo_it) {
        this.photo_it = photo_it;
    }


}
