package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Friend> allFriends = new ArrayList<>(); // полный список
    private FriendAdapter adapter;
    private EditText searchEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Log.e("FRIENDS", "onCreate FriendsActivity");

        recyclerView = findViewById(R.id.friendsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.searchEditText);

        // Добавляем TextWatcher для фильтрации
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFriends(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadFriendsFromServer();
    }

    private void loadFriendsFromServer() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/Backend/friends");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                String json = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .collect(Collectors.joining("\n"));

                Log.d("FRIENDS", json);

                Gson gson = new Gson();
                Friend[] friendArray = gson.fromJson(json, Friend[].class);
                List<Friend> friendList = Arrays.asList(friendArray);

                runOnUiThread(() -> {
                    allFriends = new ArrayList<>(friendList);
                    adapter = new FriendAdapter(this, new ArrayList<>(allFriends));
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(friend -> {
                        Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);

                        intent.putExtra("name", friend.getName());
                        intent.putExtra("surname", friend.getSurname());
                        intent.putExtra("avatarUrl", friend.getAvatarUrl()); // если есть
                        intent.putExtra("otherUserId", friend.getId());


                        startActivity(intent);
                    });

                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка загрузки друзей", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void filterFriends(String query) {
        if (adapter == null) return;

        List<Friend> filteredList = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Friend friend : allFriends) {
            if (friend.getFirstName().toLowerCase().startsWith(lowerQuery) ||
                    friend.getLastName().toLowerCase().startsWith(lowerQuery)) {
                filteredList.add(friend);
            }
        }

        adapter.updateList(filteredList);
    }
}
