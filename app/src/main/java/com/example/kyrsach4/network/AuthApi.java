package com.example.kyrsach4.network;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {

    @FormUrlEncoded
    @POST("auth")
    Call<Map<String, Object>> register(
            @Query("action") String action,
            @Field("name") String name,
            @Field("surname") String surname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth")
    Call<Map<String, Object>> login(
            @Query("action") String action,
            @Field("email") String email,
            @Field("password") String password
    );
}
