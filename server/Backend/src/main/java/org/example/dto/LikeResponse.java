package org.example.dto;

public class LikeResponse {
    public boolean success;
    public int likesCount;
    public boolean is_liked;

    public LikeResponse(boolean success, int likesCount, boolean is_liked) {
        this.success = success;
        this.likesCount = likesCount;
        this.is_liked = is_liked;
    }
}
