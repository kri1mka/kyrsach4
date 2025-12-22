package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.Adapter.PostsAdapter;
import com.example.kyrsach4.entity.Post;
import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.SessionStorage;
import com.example.kyrsach4.reqresp.LikeRequest;
import com.example.kyrsach4.reqresp.LikeResponse;

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
            finish();
            return;
        }

        initViews();
        setupRecycler();
        setupBottomNav();
        loadPosts();

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
            finish();
        });

        navHeart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SwipeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }

    private void setupRecycler() {
        postsRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PostsAdapter(
                this,
                postList,
                userIdFromPost -> {
                    Intent intent = new Intent(
                            HomeActivity.this,
                            ProfileActivityKs.class
                    );
                    intent.putExtra("user_id", userIdFromPost);
                    startActivity(intent);
                }
        );
        postsRecycler.setAdapter(adapter);
    }


    private void loadPosts() {
        ApiClient.serverApi.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    postList.clear();

                    for (Post post : response.body()) {
                        if (post.user_id != userId) {
                            postList.add(post);
                        }
                    }
                    Log.d("bbbbbbbbbbbbbbbbbbbb", "onResponse: " + response.body());

                    adapter.notifyDataSetChanged();


                    for (Post post : postList) {
                        ApiClient.serverApi.isLiked(
                                new LikeRequest(post.id, SessionStorage.userId)
                        ).enqueue(new Callback<LikeResponse>() {
                            @Override
                            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    post.is_liked = response.body().is_liked;
                                    adapter.notifyItemChanged(postList.indexOf(post));
                                }
                            }

                            @Override
                            public void onFailure(Call<LikeResponse> call, Throwable t) {}
                        });
                    }

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
