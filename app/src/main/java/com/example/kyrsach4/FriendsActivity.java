package com.example.kyrsach4;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.Adapter.FriendAdapter;
import com.example.kyrsach4.entity.Friend;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends); // ваш layout с RecyclerView

        recyclerView = findViewById(R.id.friendsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFriendsFromServer();
    }

    private void loadFriendsFromServer() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/Backend/friends"); // 10.0.2.2 для эмулятора
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                String json = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .collect(Collectors.joining("\n"));

                Gson gson = new Gson();
                Friend[] friendArray = gson.fromJson(json, Friend[].class);
                List<Friend> friendList = Arrays.asList(friendArray);

                runOnUiThread(() -> {
                    FriendAdapter adapter = new FriendAdapter(this, friendList);
                    recyclerView.setAdapter(adapter);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Ошибка загрузки друзей", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
