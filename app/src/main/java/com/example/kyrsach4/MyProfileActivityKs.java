package com.example.kyrsach4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivityKs extends AppCompatActivity {

    // Элементы интерфейса
    private TextView userName, userAge, userLocation, userTripType;
    private TextView numbOfPublications, numbOfFriends;
    private RecyclerView rvPhotos, rvTrips;
    private TextView tvEmptyPhotos, tvEmptyTrips;
    private View indicatorPhotos, indicatorTrips;
    private ImageButton bthPhotos, btnTrips;

    // Навигация
    private ImageButton btnBack, btnBell;
    private ImageButton navHome, navChat, navHeart, navTranslate, navProfile;

    // Данные
    private List<Integer> photoList;
    private List<TripKs> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ВАЖНО: проверьте точное имя вашего XML файла!
        setContentView(R.layout.activity_my_pfofile_ks);
        // Если вышеуказанное не работает, попробуйте:
        // setContentView(R.layout.activity_my_pfofile_ks); // если такое имя

        // Инициализация элементов
        initViews();

        // Проверка всех найденных элементов
        checkViews();

        // Загрузка данных пользователя
        loadUserData();

        // Настройка адаптеров
        setupAdapters();

        // Установка обработчиков событий
        setupClickListeners();

        // Изначально показываем фото
        showPhotos();
    }

    private void initViews() {
        // Основные элементы
        userName = findViewById(R.id.user_name);
        userAge = findViewById(R.id.user_age);
        userLocation = findViewById(R.id.user_location);
        userTripType = findViewById(R.id.type_of_trip);
        numbOfPublications = findViewById(R.id.numb_of_publications);
        numbOfFriends = findViewById(R.id.numb_of_friends);

        // Кнопки действий
        btnBack = findViewById(R.id.btn_back);
        btnBell = findViewById(R.id.btn_bell);
        AppCompatButton btnChange = findViewById(R.id.btn_change);
        btnChange.setOnClickListener(v ->
                startActivity(new Intent(this, ChangeDataActivityKs.class))
        );
        AppCompatButton btnTravelHistory = findViewById(R.id.btn_travel_history);
        btnTravelHistory.setOnClickListener(v ->
                startActivity(new Intent(this, TravelHistoryActivityKs.class))
        );
        ImageButton btnAddImage = findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(v ->
                startActivity(new Intent(this, CreatePublicationActivityKs.class))
        );

        // Переключение контента
        rvPhotos = findViewById(R.id.rv_photos);
        rvTrips = findViewById(R.id.rv_trips);
        tvEmptyPhotos = findViewById(R.id.tv_empty);
        tvEmptyTrips = findViewById(R.id.tv_empty_trips);
        indicatorPhotos = findViewById(R.id.indicator_photos);
        indicatorTrips = findViewById(R.id.indicator_trips);
        bthPhotos = findViewById(R.id.bth_photos);
        btnTrips = findViewById(R.id.btn_trips);

        // Нижняя навигация
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navHeart = findViewById(R.id.nav_heart);
        navTranslate = findViewById(R.id.nav_translate);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void checkViews() {
        // Проверка обязательных элементов
        if (rvPhotos == null) {
            throw new RuntimeException("rv_photos не найден! Проверь XML и setContentView");
        }

        if (rvTrips == null) {
            throw new RuntimeException("rv_trips не найден!");
        }

        if (userName == null) {
            throw new RuntimeException("user_name не найден!");
        }

        // Можно добавить проверку других важных элементов
    }

    private void loadUserData() {
        // Загрузка данных из SharedPreferences (пример)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String name = prefs.getString("name", "Имя");
        String surname = prefs.getString("surname", "Фамилия");
        String age = prefs.getString("age", "возраст");
        String location = prefs.getString("cityCountry", "город, страна");
        String tripType = prefs.getString("travelType", "вид путешествия");

        // Установка данных
        userName.setText(name + " " + surname);
        userAge.setText(age);
        userLocation.setText(location);
        userTripType.setText(tripType);

        // Загрузка примерных данных для фото и поездок
        photoList = getSamplePhotoList();
        tripList = getSampleTripList();

        // Обновление статистики (можно брать из реальных данных)
        updateStats();
    }

    private void updateStats() {
        // Здесь можно обновить статистику из реальных данных
        // Например, из базы данных или API
        int publicationsCount = photoList.size();
        int friendsCount = 433; // Примерное значение

        numbOfPublications.setText(String.valueOf(publicationsCount));
        numbOfFriends.setText(String.valueOf(friendsCount));
    }

    private void setupAdapters() {
        // Установка LayoutManager (один раз!)
        rvPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvTrips.setLayoutManager(new GridLayoutManager(this, 1));

        // Создание адаптеров
        ImageGridAdapterKs photoAdapter = new ImageGridAdapterKs(this, photoList);
        rvPhotos.setAdapter(photoAdapter);

        TripsAdapterKs tripsAdapter = new TripsAdapterKs(this, tripList);
        rvTrips.setAdapter(tripsAdapter);
    }

    private void setupClickListeners() {
        // Кнопка назад
        btnBack.setOnClickListener(v -> finish());

        // Кнопка меню/колокольчик
        btnBell.setOnClickListener(v -> {
            // Показать меню или уведомления
            Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
        });

        // Кнопка "Изменить"
        findViewById(R.id.btn_change).setOnClickListener(v ->
                startActivity(new Intent(this, ChangeDataActivityKs.class))
        );

        // Кнопка "История поездок"
        findViewById(R.id.btn_travel_history).setOnClickListener(v ->
                startActivity(new Intent(this, TravelHistoryActivityKs.class))
        );

        // Кнопка добавления фото
        findViewById(R.id.btn_add_image).setOnClickListener(v ->
                startActivity(new Intent(this, CreatePublicationActivityKs.class))
        );

        // Переключение между фото и поездками
        bthPhotos.setOnClickListener(v -> showPhotos());
        btnTrips.setOnClickListener(v -> showTrips());

        // Нижняя навигация
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        navHome.setOnClickListener(v -> {
            // Переход на главную
            Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show();
        });

        navChat.setOnClickListener(v -> {
            // Переход в чаты
            Toast.makeText(this, "Чаты", Toast.LENGTH_SHORT).show();
        });

        navHeart.setOnClickListener(v -> {
            // Переход в избранное
            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show();
        });

        navTranslate.setOnClickListener(v -> {
            // Перевод
            Toast.makeText(this, "Перевод", Toast.LENGTH_SHORT).show();
        });

        navProfile.setOnClickListener(v -> {
            // Уже на профиле
            Toast.makeText(this, "Вы уже в профиле", Toast.LENGTH_SHORT).show();
        });
    }

    private void showPhotos() {
        // Обновление индикаторов
        indicatorPhotos.setVisibility(View.VISIBLE);
        indicatorTrips.setVisibility(View.GONE);

        // Показать фото
        rvPhotos.setVisibility(View.VISIBLE);
        tvEmptyPhotos.setVisibility(photoList.isEmpty() ? View.VISIBLE : View.GONE);

        // Скрыть поездки
        rvTrips.setVisibility(View.GONE);
        tvEmptyTrips.setVisibility(View.GONE);
    }

    private void showTrips() {
        // Обновление индикаторов
        indicatorPhotos.setVisibility(View.GONE);
        indicatorTrips.setVisibility(View.VISIBLE);

        // Показать поездки
        rvTrips.setVisibility(View.VISIBLE);
        tvEmptyTrips.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);

        // Скрыть фото
        rvPhotos.setVisibility(View.GONE);
        tvEmptyPhotos.setVisibility(View.GONE);
    }

    private List<Integer> getSamplePhotoList() {
        List<Integer> list = new ArrayList<>();
        // Добавьте реальные ресурсы изображений
        // list.add(R.drawable.photo1);
        // list.add(R.drawable.photo2);
        return list;
    }

    private List<TripKs> getSampleTripList() {
        List<TripKs> list = new ArrayList<>();
        // Добавьте реальные данные о поездках
        // list.add(new TripKs("Париж", "Май 2023", R.drawable.paris));
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновить данные при возвращении на экран
        loadUserData();
    }
}