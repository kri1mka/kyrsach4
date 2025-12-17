package com.example.kyrsach4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.adapters.TripsAdapter2;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripsActivity2 extends AppCompatActivity {

    private RecyclerView rvTrips;
    private TripsAdapter2 adapter;
    private List<TripCard> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_ks);

        rvTrips = findViewById(R.id.rvTrips);

        trips = new ArrayList<>();

        adapter = new TripsAdapter2(trips, trip -> {
            // обработка клика
        });

        rvTrips.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        rvTrips.setAdapter(adapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvTrips);

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
                            trips.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(
                                    TripsActivity2.this,
                                    "Ошибка загрузки поездок",
                                    Toast.LENGTH_SHORT
                            ).show();
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
