package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.Adapter.PostsAdapter;
import com.example.kyrsach4.entity.Post;
import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.SessionStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView postsRecycler;
    private PostsAdapter adapter;
    private final List<Post> postList = new ArrayList<>();

    ImageButton navHome, navChat, navHeart, navTranslate, navProfile;
    ImageButton btnHelp;
    Integer userId = SessionStorage.userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (userId == null) {
            finish(); // пользователь не авторизован
            return;
        }

        initViews();
        setupRecycler();
        setupBottomNav();
        loadPosts(); // 🔹 ЗАГРУЗКА С СЕРВЕРА
    }

    private void initViews() {
        postsRecycler = findViewById(R.id.postsRecycler);

        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navHeart = findViewById(R.id.nav_heart);
        navTranslate = findViewById(R.id.nav_translate);
        navProfile = findViewById(R.id.nav_profile);

        btnHelp = findViewById(R.id.btnHelp);

        navChat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MessagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // чтобы закрыть HomeActivity, если нужно
        });

        navHeart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SwipeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // чтобы закрыть HomeActivity, если нужно
        });

    }


    private void setupRecycler() {
        postsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostsAdapter(this, postList);
        postsRecycler.setAdapter(adapter);
    }

    private void loadPosts() {
        ApiClient.serverApi.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this,
                            "Ошибка загрузки постов", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(HomeActivity.this,
                        "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNav() {
        navTranslate.setOnClickListener(v -> {
            startActivity(new Intent(this, TranslatorActivity.class));
        });

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MyProfileActivityKs.class));
        });

        btnHelp.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, HelpActivity.class));
        });

    }

}
