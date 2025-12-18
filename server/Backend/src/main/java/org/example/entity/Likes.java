package org.example.entity;

public class Likes {
    private Integer id;
    private Integer postId;
    private Integer userId;

    public Likes() {}

    public Likes(Integer postId, Integer userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
