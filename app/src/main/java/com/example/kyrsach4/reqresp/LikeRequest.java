package com.example.kyrsach4.reqresp;

public class LikeRequest {
    public int post_id;
    public int user_id;

    public LikeRequest(int postId, int userId) {
        this.post_id = postId;
        this.user_id = userId;
    }
}
