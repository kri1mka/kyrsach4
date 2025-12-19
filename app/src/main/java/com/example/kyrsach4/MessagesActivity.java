package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.Adapter.MessageAdapter;
import com.example.kyrsach4.entity.Message;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText searchEditText;
    private int currentUserId = 1;
    private List<Message> allMessages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_messages);

        recyclerView = findViewById(R.id.friendsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMessages(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        loadMessagesFromServer();
    }

    private void loadMessagesFromServer() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:8080/Backend/messages");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                String json = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .collect(Collectors.joining("\n"));

                Log.d("MESSAGES", json);

                Gson gson = new Gson();
                Message[] messagesArray = gson.fromJson(json, Message[].class);
                allMessages = Arrays.asList(messagesArray);

// Убираем дубликаты по имени
                List<Message> uniqueMessages = new ArrayList<>();
                Set<String> seenNames = new HashSet<>();

                for (Message message : allMessages) {
                    String fullName = message.getName() + " " + message.getSurname();
                    if (!seenNames.contains(fullName)) {
                        uniqueMessages.add(message);
                        seenNames.add(fullName);
                    }
                }

// Сортировка по времени: новые сообщения вверху
                uniqueMessages.sort((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()));

                allMessages = uniqueMessages;

// Устанавливаем адаптер
                runOnUiThread(() -> {
                    adapter = new MessageAdapter(this, uniqueMessages);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(message -> {

                        int otherUserId =
                                message.getFromUserId() == currentUserId
                                        ? message.getToUserId()
                                        : message.getFromUserId();

                        Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                        intent.putExtra("name", message.getName());
                        intent.putExtra("surname", message.getSurname());
                        intent.putExtra("avatarUrl", message.getAvatarUrl());
                        intent.putExtra("otherUserId", otherUserId);
                        startActivity(intent);

                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка загрузки сообщений", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void filterMessages(String query) {
        if (adapter == null) return;

        String lowerQuery = query.toLowerCase();
        List<Message> filtered = new ArrayList<>();
        Set<String> seenUsers = new HashSet<>(); // для уникальности

        for (Message message : allMessages) {
            String fullName = message.getName() + " " + message.getSurname();

            if ((message.getName() != null && message.getName().toLowerCase().startsWith(lowerQuery)) ||
                    (message.getSurname() != null && message.getSurname().toLowerCase().startsWith(lowerQuery))) {

                // проверяем, выводился ли этот пользователь ранее
                if (!seenUsers.contains(fullName)) {
                    filtered.add(message);
                    seenUsers.add(fullName);
                }
            }
        }

        adapter.updateList(filtered);
    }

}
