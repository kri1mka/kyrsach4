package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.core.widget.NestedScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.adapters.TripsAdapterKs;
import com.example.kyrsach4.entity.PostCard;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.entity.UserProfile;
import com.example.kyrsach4.network.ApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivityKs extends AppCompatActivity {

    private TextView userName, userAge, userLocation, typeOfTrip, numbOfPublications;
    private LinearLayout llBio;
    private ImageButton btnPosts, btnTrips;
    private View indicatorPosts, indicatorTrips;
    private NestedScrollView scrollPosts;
    private RecyclerView rvTrips;
    private LinearLayout postsContainer;
    private TextView tvEmptyPosts, tvEmptyTrips;
    private TripsAdapterKs tripsAdapter;

    private TextView btnSubscribe;
    private TextView tvFollowersCount;

    // Навигация
    private ImageButton btnBack, btnBell;
    private ImageButton navHome, navChat, navHeart, navTranslate, navProfile;

    private ImageView ivAvatar;
    private boolean isFriend = false;
    private int followersCount = 120;

    private int userId = 1; // TODO: передавать реальный userId через Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ks);

        initViews();
        setupListeners();
        loadUserProfile();
        showPosts();
    }

    private void initViews() {
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        userLocation = findViewById(R.id.user_location);
        typeOfTrip = findViewById(R.id.type_of_trip);
        numbOfPublications = findViewById(R.id.numb_of_publications);
        llBio = findViewById(R.id.ll_bio);
        ivAvatar = findViewById(R.id.iv_avatar);


        btnPosts = findViewById(R.id.btn_posts);
        btnTrips = findViewById(R.id.btn_trips);
        indicatorPosts = findViewById(R.id.indicator_posts);
        indicatorTrips = findViewById(R.id.indicator_trips);

        scrollPosts = findViewById(R.id.scroll_posts);
        postsContainer = findViewById(R.id.posts_container);
        rvTrips = findViewById(R.id.rv_trips);
        tvEmptyPosts = findViewById(R.id.tv_empty_posts);
        tvEmptyTrips = findViewById(R.id.tv_empty_trips);

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        tripsAdapter = new TripsAdapterKs(null);
        rvTrips.setAdapter(tripsAdapter);

        btnSubscribe = findViewById(R.id.btn_subscribe);
        tvFollowersCount = findViewById(R.id.tv_followers_count);

        tvFollowersCount.setText(String.valueOf(followersCount));
        updateSubscribeButton();

        // Нижняя навигация
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navHeart = findViewById(R.id.nav_heart);
        navTranslate = findViewById(R.id.nav_translate);
        navProfile = findViewById(R.id.nav_profile);

    }
    private void setupListeners() {
        btnPosts.setOnClickListener(v -> showPosts());
        btnTrips.setOnClickListener(v -> showTrips());
        btnSubscribe.setOnClickListener(v -> {
            if (!isFriend) {
                subscribe();
            } else {
                showUnsubscribeDialog();
            }
        });
        setupBottomNavigation();
    }

    private void showPosts() {
        scrollPosts.setVisibility(View.VISIBLE);
        rvTrips.setVisibility(View.GONE);
        tvEmptyPosts.setVisibility(View.GONE);
        tvEmptyTrips.setVisibility(View.GONE);

        indicatorPosts.setVisibility(View.VISIBLE);
        indicatorTrips.setVisibility(View.GONE);
        btnPosts.setColorFilter(getResources().getColor(R.color.primary));
        btnTrips.setColorFilter(getResources().getColor(R.color.text_hint));

        ApiClient.serverApi.getUserPosts(userId).enqueue(new Callback<List<PostCard>>() {
            @Override
            public void onResponse(Call<List<PostCard>> call, Response<List<PostCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostCard> posts = response.body();
                    numbOfPublications.setText(String.valueOf(posts.size()));
                    postsContainer.removeAllViews();

                    if (posts.isEmpty()) {
                        tvEmptyPosts.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (PostCard post : posts) {
                        View postView = getLayoutInflater().inflate(R.layout.item_post_ks, postsContainer, false);
                        bindPost(postView, post);
                        postsContainer.addView(postView);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostCard>> call, Throwable t) {
                Toast.makeText(ProfileActivityKs.this, "Ошибка загрузки постов", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindPost(View postView, PostCard post) {
        TextView username = postView.findViewById(R.id.post_username);
        TextView location = postView.findViewById(R.id.post_location);
        ImageView image = postView.findViewById(R.id.post_image);
        TextView description = postView.findViewById(R.id.post_description);
        TextView likesCount = postView.findViewById(R.id.likes_count);
        TextView postDate = postView.findViewById(R.id.post_date);
        ImageView postAvatar = postView.findViewById(R.id.post_avatar);

        ImageButton btnLike = postView.findViewById(R.id.btn_like);

        // Имя пользователя
        username.setText(post.getUserName() != null ? post.getUserName() : "Пользователь");

        // Локация
        location.setText(post.getLocation() != null ? post.getLocation() : "");

        // Описание
        description.setText(post.getDescription() != null ? post.getDescription() : "");

        // Лайки
        likesCount.setText(String.valueOf(post.getLikesCount() != null ? post.getLikesCount() : 0));

        // Фото
        if (post.getPhotoIt() != null && !post.getPhotoIt().isEmpty()) {
            String photoUrl = post.getPhotoIt().replaceAll("\\s+", "%20").trim();
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.sample_photo1)
                    .into(image);
        } else {
            image.setImageResource(R.drawable.sample_photo1);
        }
        // Дата
        if (post.getCreatedAt() != null && !post.getCreatedAt().isEmpty()) {
            try {
                String cleaned = post.getCreatedAt().replaceAll("\\s+", " ").trim();
                SimpleDateFormat serverFormat = new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a", Locale.ENGLISH);
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                Date date = serverFormat.parse(cleaned);
                postDate.setText(displayFormat.format(date));
            } catch (Exception e) {
                postDate.setText("Только что");
            }
        } else {
            postDate.setText("Только что");
        }

        if (post.getPhoto() != null && !post.getPhoto().isEmpty()) {
            Glide.with(postView)
                    .load(post.getPhoto())
                    .placeholder(R.drawable.pngtreecat_default_avatar_5416936)
                    .into(postAvatar);
        } else {
            postAvatar.setImageResource(R.drawable.pngtreecat_default_avatar_5416936);
        }
        int likes = post.getLikesCount() != null ? post.getLikesCount() : 0;
        likesCount.setText(String.valueOf(likes));

// начальное состояние иконки
        updateLikeUI(post.isLiked(), btnLike);

        btnLike.setOnClickListener(v -> {
            boolean liked = post.isLiked();

            if (!liked) {
                // лайк
                post.setLiked(true);
                post.setLikesCount(likes + 1);
            } else {
                // анлайк
                post.setLiked(false);
                post.setLikesCount(likes);
                post.setLikesCount(post.getLikesCount());
            }

            likesCount.setText(String.valueOf(post.getLikesCount()));
            updateLikeUI(post.isLiked(), btnLike);
        });

    }

    private void updateLikeUI(boolean liked, ImageButton btnLike) {
        if (liked) {
            btnLike.setImageResource(R.drawable.icons8_aime_rempli_24);
            btnLike.setColorFilter(getResources().getColor(R.color.error));
        } else {
            btnLike.setImageResource(R.drawable.icons8_aime_rempli_24);
            btnLike.setColorFilter(getResources().getColor(R.color.text_primary));
        }
    }

    private void showTrips() {
        scrollPosts.setVisibility(View.GONE);
        rvTrips.setVisibility(View.VISIBLE);
        tvEmptyPosts.setVisibility(View.GONE);
        tvEmptyTrips.setVisibility(View.GONE);

        indicatorPosts.setVisibility(View.GONE);
        indicatorTrips.setVisibility(View.VISIBLE);

        btnPosts.setColorFilter(getResources().getColor(R.color.text_hint));
        btnTrips.setColorFilter(getResources().getColor(R.color.primary));

        ApiClient.serverApi.getUserTrips(userId).enqueue(new Callback<List<TripCard>>() {
            @Override
            public void onResponse(Call<List<TripCard>> call, Response<List<TripCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TripCard> trips = response.body();

                    // Фильтруем поездки: оставляем только те, у которых endDate >= текущая дата
                    long now = System.currentTimeMillis();
                    List<TripCard> futureTrips = new ArrayList<>();
                    for (TripCard trip : trips) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date tripEnd = sdf.parse(trip.getEndDate());
                            if (tripEnd != null && tripEnd.getTime() >= now) {
                                futureTrips.add(trip);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (futureTrips.isEmpty()) {
                        tvEmptyTrips.setVisibility(View.VISIBLE);
                    }
                    tripsAdapter.updateData(futureTrips);
                }
            }

            @Override
            public void onFailure(Call<List<TripCard>> call, Throwable t) {
                Toast.makeText(ProfileActivityKs.this, "Ошибка загрузки поездок", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loadUserProfile() {
        ApiClient.serverApi.getUserProfile(userId).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body();
                    userName.setText(profile.getFullName());
                    userAge.setText(profile.getAge() != null ? String.valueOf(profile.getAge()) : "—");
                    userLocation.setText(profile.getLocation() != null ? profile.getLocation() : "—");
                    typeOfTrip.setText(profile.getTravelType() != null ? profile.getTravelType() : "—");
                    if (profile.getPhoto() != null && !profile.getPhoto().isEmpty()) {
                        Glide.with(ProfileActivityKs.this)
                                .load(profile.getPhoto())
                                .placeholder(R.drawable.pngtreecat_default_avatar_5416936)
                                .into(ivAvatar);
                    } else {
                        ivAvatar.setImageResource(R.drawable.pngtreecat_default_avatar_5416936);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivityKs.this, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void subscribe() {
        isFriend = true;
        followersCount++;
        tvFollowersCount.setText(String.valueOf(followersCount));
        updateSubscribeButton();
    }

    private void showUnsubscribeDialog() {
        androidx.appcompat.app.AlertDialog dialog =
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Отписаться")
                        .setMessage("Вы уверены, что хотите отписаться?")
                        .setPositiveButton("Да", (d, which) -> unsubscribe())
                        .setNegativeButton("Нет", null)
                        .create();

        dialog.show();

        // Меняем цвет текста кнопок на primary
        int primaryColor = getResources().getColor(R.color.primary);
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(primaryColor);
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(primaryColor);

        // Опционально: закругляем фон диалога
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_ks);
    }

    private void unsubscribe() {
        isFriend = false;
        followersCount--;
        tvFollowersCount.setText(String.valueOf(followersCount));
        updateSubscribeButton();
    }


    private void updateSubscribeButton() {
        if (isFriend) {
            btnSubscribe.setText("Друзья");
            btnSubscribe.setBackgroundResource(R.drawable.button_outline_ks);
            btnSubscribe.setTextColor(getResources().getColor(R.color.primary));
        } else {
            btnSubscribe.setText("Подписаться");
            btnSubscribe.setBackgroundResource(R.drawable.button_ks);
            btnSubscribe.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private void setupBottomNavigation() {
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
    }
}