package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.adapters.TripsAdapter2;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.network.ApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsActivity2 extends AppCompatActivity {

    private RecyclerView rvTrips;
    private TripsAdapter2 adapter;
    private List<TripCard> trips;
    private ImageButton btnBack;
    private ImageButton navHome, navChat, navHeart, navTranslate, navProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_ks);

        // Инициализация RecyclerView
        rvTrips = findViewById(R.id.rvTrips);
        trips = new ArrayList<>();
        adapter = new TripsAdapter2(trips, trip -> {
            // обработка клика
        });
        rvTrips.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrips.setAdapter(adapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvTrips);

        // ❗ Инициализация кнопок
        btnBack = findViewById(R.id.btn_back);
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navHeart = findViewById(R.id.nav_heart);
        navTranslate = findViewById(R.id.nav_translate);
        navProfile = findViewById(R.id.nav_profile);

        btnBack.setOnClickListener(v -> finish());

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        navChat.setOnClickListener(v -> {
            Toast.makeText(this, "Чат", Toast.LENGTH_SHORT).show();
        });

        navHeart.setOnClickListener(v -> {
            Toast.makeText(this, "Свапы", Toast.LENGTH_SHORT).show();
        });

        navTranslate.setOnClickListener(v -> {
            Toast.makeText(this, "Перевод", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            // Уже на профиле
            Toast.makeText(this, "Вы уже в профиле", Toast.LENGTH_SHORT).show();
        });

        // Загрузка поездок
        loadUserTrips();
    }


    private void loadUserTrips() {

        int userId = 1; // ❗ временно. Потом возьмёшь из сессии

        ApiClient.api.getUserTrips(userId)
                .enqueue(new Callback<List<TripCard>>() {

                    @Override
                    public void onResponse(Call<List<TripCard>> call,
                                           Response<List<TripCard>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            trips.clear();
                            long now = System.currentTimeMillis();

                            for (TripCard trip : response.body()) {
                                if (isPastTrip(trip, now)) {
                                    trips.add(trip);
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                    private boolean isPastTrip(TripCard trip, long now) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            long end = sdf.parse(trip.getEndDate()).getTime();
                            return end < now;
                        } catch (Exception e) {
                            return false;
                        }
                    }


                    @Override
                    public void onFailure(Call<List<TripCard>> call, Throwable t) {
                        Toast.makeText(
                                TripsActivity2.this,
                                "Сервер недоступен",
                                Toast.LENGTH_SHORT
                        ).show();

                        Log.e("API_ERROR", t.getMessage(), t);
                    }
                });
    }
}
