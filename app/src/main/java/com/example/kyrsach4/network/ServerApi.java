package com.example.kyrsach4.network;

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
import retrofit2.http.*;

public interface ServerApi {

    @GET("users")
    Call<List<Users>> getTeams();

    @GET("api/users/{userId}")
    Call<UserProfile> getUserProfile(@Path("userId") int userId);

    @GET("api/posts/{userId}")
    Call<List<PostCard>> getUserPosts(@Path("userId") int userId);

    @GET("api/users/trips/{userId}")
    Call<List<TripCard>> getUserTrips(@Path("userId") int userId);

    @DELETE("users/{id}")
    Call<Void> deleteTeam(@Path("id") int id);

    @GET("api/posts")
    Call<List<PostCard>> getAllPosts(@Query("page") int page, @Query("limit") int limit);

    @POST("api/posts")
    Call<PostCard> createPost(@Body PostCard post);

    @POST("api/posts/{postId}/like")
    Call<Map<String, Object>> likePost(@Path("postId") int postId, @Query("like") boolean like);

    @DELETE("api/posts/{postId}")
    Call<Void> deletePost(@Path("postId") int postId);

    @PUT("api/users/{id}")
    Call<Void> updateProfile(@Path("id") int userId, @Body UpdateProfileRequest request);

    @Multipart
    @POST("api/posts/upload")
    Call<Map<String, String>> uploadPostImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/users/trips/images")
    Call<Map<String, String>> uploadTripImage(@Part MultipartBody.Part file);

    // Создание поездки через multipart
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
