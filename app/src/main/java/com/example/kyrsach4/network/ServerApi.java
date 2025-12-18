package com.example.kyrsach4.network;

import com.example.kyrsach4.entity.Post;
import com.example.kyrsach4.reqresp.LoginRequest;
import com.example.kyrsach4.reqresp.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApi {

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("posts")
    Call<List<Post>> getPosts();
}
