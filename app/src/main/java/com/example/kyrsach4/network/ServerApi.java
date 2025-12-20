package com.example.kyrsach4.network;

import com.example.kyrsach4.entity.Post;
import com.example.kyrsach4.reqresp.LoginRequest;
import com.example.kyrsach4.reqresp.RegisterRequest;
import com.example.kyrsach4.dto.UpdateProfileRequest;
import com.example.kyrsach4.entity.PostCard;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.entity.UserProfile;
import com.example.kyrsach4.entity.Users;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerApi {

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @GET("api/users/{userId}")
    Call<UserProfile> getUserProfile(@Path("userId") int userId);

    // Исправленные пути для постов и поездок
    @GET("api/posts/{userId}")
    Call<List<PostCard>> getUserPosts(@Path("userId") int userId);

    @GET("api/users/trips/{userId}")
    Call<List<TripCard>> getUserTrips(@Path("userId") int userId);

    @DELETE("users/{id}")
    Call<Void> deleteTeam(@Path("id") int id);

    // Получение всех постов (для ленты)
    @GET("api/posts")
    Call<List<PostCard>> getAllPosts(
            @Query("page") int page,
            @Query("limit") int limit
    );

    // Создание поста
    @POST("api/posts")
    Call<PostCard> createPost(@Body PostCard post);

    // Лайк поста
    @POST("api/posts/{postId}/like")
    Call<Map<String, Object>> likePost(
            @Path("postId") int postId,
            @Query("like") boolean like
    );

    // Удаление поста
    @DELETE("api/posts/{postId}")
    Call<Void> deletePost(@Path("postId") int postId);

    @PUT("api/users/{id}")
    Call<Void> updateProfile(
            @Path("id") int userId,
            @Body UpdateProfileRequest request
    );

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("posts")
    Call<List<Post>> getPosts();

    @Multipart
    @POST("api/posts/upload")
    Call<Map<String, String>> uploadPostImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/posts")
    Call<PostCard> createPostMultipart(
            @Part("user_id") RequestBody userId,
            @Part("location") RequestBody location,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("api/users/trips/images")
    Call<Map<String, String>> uploadTripImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/users/trips")
    Call<TripCard> createTripMultipart(
            @Part("user_id") RequestBody userId,
            @Part("location") RequestBody location,
            @Part("startDate") RequestBody startDate,
            @Part("endDate") RequestBody endDate,
            @Part("type") RequestBody type,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );
}
