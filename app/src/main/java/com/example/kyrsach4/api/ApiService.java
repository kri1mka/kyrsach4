package com.example.kyrsach4.api;

import com.example.kyrsach4.entities.TripCard;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("tripcards")
    Call<List<TripCard>> getTripCards();


        @GET("/cards")
        Call<List<TripCard>> getCards();

        @POST("/swipe")
        Call<Void> sendSwipe(
                @Query("card_id") int cardId,
                @Query("liked") boolean liked
        );
    }


