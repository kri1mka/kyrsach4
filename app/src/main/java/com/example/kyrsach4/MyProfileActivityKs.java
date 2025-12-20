package com.example.kyrsach4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.adapters.PostAdapterKs;
import com.example.kyrsach4.adapters.TripsAdapterKs;
import com.example.kyrsach4.entity.PostCard;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.entity.UserProfile;
import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.SessionStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivityKs extends AppCompatActivity {

    // Элементы интерфейса
    private TextView userName, userAge, userLocation, userTripType;
    private TextView numbOfPublications, numbOfFriends;
    private LinearLayout postsContainer;
    private RecyclerView rvTrips;
    private TextView tvEmptyPosts, tvEmptyTrips;
    private View indicatorPosts, indicatorTrips;
    private ImageButton btnPosts, btnTrips;
    private TextView tvFriends;

    // Навигация
    private ImageButton btnBack, btnBell;
    private ImageButton navHome, navChat, navHeart, navTranslate, navProfile;

    // Данные и адаптеры
    private List<PostCard> postList = new ArrayList<>();
    private List<TripCard> tripList = new ArrayList<>();
    private TripsAdapterKs tripsAdapter;
    private PostAdapterKs postAdapter;
    private ImageView ivAvatar;

    private Integer currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pfofile_ks);
        tvFriends = findViewById(R.id.tvFriends);
        tvFriends.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivityKs.this, FriendsActivity.class);
            startActivity(intent);
        });

        currentUserId = SessionStorage.userId;

        if (currentUserId == null) {
            // пользователь не авторизован
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        // Инициализация элементов
        initViews();
        tvEmptyPosts.setVisibility(View.GONE);
        //tvEmptyTrips.setVisibility(View.GONE);


        // Инициализация адаптеров
        tripsAdapter = new TripsAdapterKs(tripList);
        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        rvTrips.setAdapter(tripsAdapter);

        // Загрузка данных
        loadUserData();
        loadUserPosts();
        loadUserTrips();

        // Установка обработчиков событий
        setupClickListeners();

        // Изначально показываем посты
        showPosts();
    }

    private void initViews() {
        // Основные элементы
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        userLocation = findViewById(R.id.user_location);
        userTripType = findViewById(R.id.type_of_trip);
        numbOfPublications = findViewById(R.id.numb_of_publications);
        numbOfFriends = findViewById(R.id.tv_followers_count);
        ivAvatar = findViewById(R.id.iv_avatar);

        // Контейнеры контента
        postsContainer = findViewById(R.id.posts_container);
        rvTrips = findViewById(R.id.rv_trips);
        tvEmptyPosts = findViewById(R.id.tv_empty_posts);
        tvEmptyTrips = findViewById(R.id.tv_empty_trips);

        // Индикаторы и кнопки переключения
        indicatorPosts = findViewById(R.id.indicator_posts);
        indicatorTrips = findViewById(R.id.indicator_trips);
        btnPosts = findViewById(R.id.btn_posts);
        btnTrips = findViewById(R.id.btn_trips);

        // Кнопки действий
        btnBack = findViewById(R.id.btn_back);
        btnBell = findViewById(R.id.btn_bell);
        // Нижняя навигация
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navHeart = findViewById(R.id.nav_heart);
        navTranslate = findViewById(R.id.nav_translate);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void loadUserData() {
        ApiClient.serverApi.getUserProfile(currentUserId).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body();
                    updateUserUI(user);
                } else {
                    Toast.makeText(MyProfileActivityKs.this,
                            "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(MyProfileActivityKs.this,
                        "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserPosts() {
        ApiClient.serverApi.getUserPosts(currentUserId).enqueue(new Callback<List<PostCard>>() {
            @Override
            public void onResponse(Call<List<PostCard>> call, Response<List<PostCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    updatePostsUI();

                    // Обновляем количество публикаций
                    numbOfPublications.setText(String.valueOf(postList.size()));
                }
            }

            @Override
            public void onFailure(Call<List<PostCard>> call, Throwable t) {
                Toast.makeText(MyProfileActivityKs.this,
                        "Ошибка загрузки постов", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserTrips() {
        ApiClient.serverApi.getUserTrips(currentUserId)
                .enqueue(new Callback<List<TripCard>>() {
                    @Override
                    public void onResponse(Call<List<TripCard>> call, Response<List<TripCard>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            tripList.clear();

                            long now = System.currentTimeMillis();

                            for (TripCard trip : response.body()) {
                                if (isFutureTrip(trip, now)) {
                                    tripList.add(trip);
                                }
                            }

                            tripsAdapter.updateData(tripList);
                            //tvEmptyTrips.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TripCard>> call, Throwable t) {
                        Toast.makeText(MyProfileActivityKs.this,
                                "Ошибка загрузки поездок", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean isFutureTrip(TripCard trip, long now) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long start = sdf.parse(trip.getStartDate()).getTime();
            return start > now;
        } catch (Exception e) {
            return false;
        }
    }



    private void updateUserUI(UserProfile user) {
        userName.setText(user.getFullName() != null ? user.getFullName() : "Пользователь");
        userAge.setText(user.getAge() != null ? user.getAge() + " лет" : "—");
        userLocation.setText(user.getLocation() != null ? user.getLocation() : "—");
        userTripType.setText(user.getTravelType() != null ? user.getTravelType() : "—");
        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
            Glide.with(MyProfileActivityKs.this)
                    .load(user.getPhoto())
                    .placeholder(R.drawable.pngtreecat_default_avatar_5416936)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageResource(R.drawable.pngtreecat_default_avatar_5416936);
        }
    }



    private void updatePostsUI() {
        postsContainer.removeAllViews();

        if (postList.isEmpty()) {
            tvEmptyPosts.setVisibility(View.VISIBLE);
            return;
        }
        tvEmptyPosts.setVisibility(View.GONE);

        for (PostCard post : postList) {
            View postView = getLayoutInflater().inflate(R.layout.item_post_ks, postsContainer, false);

            bindPost(postView, post);

            postsContainer.addView(postView);
        }
    }

    private void setupClickListeners() {
        // Кнопка назад
        btnBack.setOnClickListener(v -> finish());

        // Переключение между постами и поездками
        btnPosts.setOnClickListener(v -> showPosts());
        btnTrips.setOnClickListener(v -> showTrips());

        // Кнопка "Изменить"
        findViewById(R.id.btn_change).setOnClickListener(v ->
                startActivity(new Intent(this, ChangeDataActivityKs.class))
        );

        // Кнопка "История поездок"
        findViewById(R.id.btn_travel_history).setOnClickListener(v ->
                startActivity(new Intent(this, TripsActivity2.class))
        );

        // Кнопка добавления фото
        findViewById(R.id.btn_add_image).setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePublicationActivityKs.class);
            intent.putExtra("userId", SessionStorage.userId);
            startActivity(intent);
        });


        // Настройка нижней навигации
        setupBottomNavigation();
    }

    private void showPosts() {
        indicatorPosts.setVisibility(View.VISIBLE);
        indicatorTrips.setVisibility(View.GONE);

        btnPosts.setColorFilter(getResources().getColor(R.color.primary));
        btnTrips.setColorFilter(getResources().getColor(R.color.text_hint));

        postsContainer.setVisibility(View.VISIBLE);

        rvTrips.setVisibility(View.GONE);
        tvEmptyTrips.setVisibility(View.GONE);
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

        // Данные
        username.setText(post.getUserName() != null ? post.getUserName() : "Пользователь");
        location.setText(post.getLocation() != null ? post.getLocation() : "");
        description.setText(post.getDescription() != null ? post.getDescription() : "");
        int likes = post.getLikesCount() != null ? post.getLikesCount() : 0;
        likesCount.setText(String.valueOf(likes));

        // Фото поста
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
                postDate.setText(displayFormat.format(serverFormat.parse(cleaned)));
            } catch (Exception e) {
                postDate.setText("Только что");
            }
        } else {
            postDate.setText("Только что");
        }

        // Аватарка пользователя
        if (post.getAvatarUrl() != null && !post.getAvatarUrl().isEmpty()) {
            Glide.with(this)
                    .load(post.getAvatarUrl())
                    .circleCrop()
                    .placeholder(R.drawable.pngtreecat_default_avatar_5416936)
                    .error(R.drawable.pngtreecat_default_avatar_5416936)
                    .into(postAvatar);
        } else {
            postAvatar.setImageResource(R.drawable.pngtreecat_default_avatar_5416936);
        }

        // Инициализация лайка
        updateLikeUI(post.isLiked(), btnLike);
        btnLike.setOnClickListener(v -> {
            if (!post.isLiked()) {
                post.setLiked(true);
                post.setLikesCount(likes + 1);
            } else {
                post.setLiked(false);
                post.setLikesCount(likes);
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
        indicatorPosts.setVisibility(View.GONE);
        indicatorTrips.setVisibility(View.VISIBLE);

        btnPosts.setColorFilter(getResources().getColor(R.color.text_hint));
        btnTrips.setColorFilter(getResources().getColor(R.color.primary));

        rvTrips.setVisibility(View.VISIBLE);
        postsContainer.setVisibility(View.GONE);

        tvEmptyPosts.setVisibility(View.GONE);

        if (tripList.isEmpty()) {
            tvEmptyTrips.setVisibility(View.VISIBLE);
        } else {
            tvEmptyTrips.setVisibility(View.GONE);
        }
    }



    private void setupBottomNavigation() {
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        navChat.setOnClickListener(v -> {
            startActivity(new Intent(this, MessagesActivity.class));
            finish();
        });

        navHeart.setOnClickListener(v -> {
            startActivity(new Intent(this, SwipeActivity.class));
            finish();
        });

        navTranslate.setOnClickListener(v -> {
            startActivity(new Intent(this, TranslatorActivity.class));
            finish();
        });

        navProfile.setOnClickListener(v -> {
            // Уже на профиле
            Toast.makeText(this, "Вы уже в профиле", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновить данные при возвращении на экран
        loadUserData();
        loadUserPosts();
        loadUserTrips();
    }
}