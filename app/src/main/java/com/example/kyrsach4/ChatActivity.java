package com.example.kyrsach4;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.kyrsach4.Adapter.ChatAdapter;
import com.example.kyrsach4.entity.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;

    private EditText messageInput;
    private ImageButton sendButton;

    private int currentUserId = 1; // свой ID, можно получить из профиля/сессии
    private int otherUserId;

    private final String SERVER_URL = "http://10.0.2.2:8080/Backend/chat"; // локальный сервер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            finish(); // ← возвращает на MessageActivity
        });

        hideKeyboard();

        recyclerView = findViewById(R.id.messagesRecycler);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        TextView userName = findViewById(R.id.userName);
        ImageView avatarImage = findViewById(R.id.avatarImage);

        // Получаем данные о выбранном пользователе из Intent
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String avatarUrl = getIntent().getStringExtra("avatarUrl");
        otherUserId = getIntent().getIntExtra("otherUserId", 2);

        userName.setText(firstName + " " + lastName);

        // Загружаем аватар через Glide с круговой маской
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            String fullAvatarUrl = "http://10.0.2.2:8080/Backend/avatar?file=" + avatarUrl;
            Glide.with(this)
                    .load(fullAvatarUrl)
                    .circleCrop()
                    .placeholder(R.drawable.bg_item_trip_ks) // плейсхолдер, пока грузится
                    .into(avatarImage);
        }

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        new Thread(() -> {
            try {
                URL url = new URL(SERVER_URL + "?user1=" + currentUserId + "&user2=" + otherUserId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                Type listType = new TypeToken<List<Message>>() {}.getType();
                List<Message> messages = new Gson().fromJson(sb.toString(), listType);

                runOnUiThread(() -> {
                    messageList.clear();
                    messageList.addAll(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        new Thread(() -> {
            try {
                String params = "fromUser=" + currentUserId +
                        "&toUser=" + otherUserId +
                        "&message=" + URLEncoder.encode(text, "UTF-8");
                URL url = new URL(SERVER_URL + "?" + params);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.getInputStream().close();

                runOnUiThread(() -> {
                    messageInput.setText("");
                    loadMessages();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
