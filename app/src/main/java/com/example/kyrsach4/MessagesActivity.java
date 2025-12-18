package com.example.kyrsach4;

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
import java.util.List;
import java.util.stream.Collectors;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private EditText searchEditText;

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

                runOnUiThread(() -> {
                    adapter = new MessageAdapter(this, allMessages);
                    recyclerView.setAdapter(adapter);
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

        for (Message message : allMessages) {

            String firstName = message.getFirstName();
            String lastName = message.getLastName();

            if ((firstName != null && firstName.toLowerCase().startsWith(lowerQuery)) ||
                    (lastName != null && lastName.toLowerCase().startsWith(lowerQuery))) {
                filtered.add(message);
            }
        }

        adapter.updateList(filtered);
    }

}
