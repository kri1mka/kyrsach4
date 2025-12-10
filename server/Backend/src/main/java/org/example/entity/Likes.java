package org.example.entity;

public class Likes {
    private Integer id;
    private Integer post_id;
    private Integer user_id;

    public Likes() {}

    public Likes(Integer post_id, Integer user_id) {
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
