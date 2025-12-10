package com.example.kyrsach4.network;

import com.example.kyrsach4.entity.Users;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ServerApi {

    @GET("users")
    Call<List<Users>> getTeams();

    @DELETE("users/{id}")
    Call<Void> deleteTeam(@Path("id") int id);
}
