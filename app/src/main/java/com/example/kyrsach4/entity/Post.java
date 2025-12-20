package com.example.kyrsach4.entity;

public class Post {
    public int id;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", likesCount=" + likes_count +
                ", photoUrl='" + photoUrl + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    public int user_id;
    public String userName;
    public String description;
    public String location;
    public String createdAt;
    public int likes_count;
    public String photoUrl;
    public String avatarUrl;
}
