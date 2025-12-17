package com.example.kyrsach4.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new SessionInterceptor())
            .addInterceptor(logging)
            .build();

    private static final String BASE_URL = "http://10.0.2.2:8080/Backend/";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    public static final AuthApi authApi = ApiClient.retrofit.create(AuthApi.class);
}
