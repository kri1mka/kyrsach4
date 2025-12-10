package com.example.kyrsach4;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivityKs extends AppCompatActivity {

    private RecyclerView rvPhotos, rvTrips;
    private TextView tvEmptyPhotos, tvEmptyTrips;
    private View indicatorPhotos, indicatorTrips;
    private ImageButton bthPhotos, btnTrips;
    private AppCompatButton btnFollow, btnMessage, btnReviews;
    private boolean isFollowing = false;

    private List<Integer> photoList;
    private List<TripKs> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ks);

        // Инициализация всех элементов
        initViews();

        // Проверка, что элементы найдены
        checkViews();

        // Настройка адаптеров
        setupAdapters();

        // Установка обработчиков событий
        setupClickListeners();

        // Загрузка данных
        loadData();

        // Изначально показываем фото
        showPhotos();
    }

    private void initViews() {
        // Кнопки верхней панели
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnBell = findViewById(R.id.btn_bell);
        btnBell.setOnClickListener(v -> showMenu());

        // Основные кнопки действий
        btnFollow = findViewById(R.id.btn_change);
        btnMessage = findViewById(R.id.btn_travel_history); // Это ImageButton, а не AppCompatButton
        btnReviews = findViewById(R.id.btn_reviews);

        // Элементы для переключения контента
        rvPhotos = findViewById(R.id.rv_photos);
        rvTrips = findViewById(R.id.rv_trips);
        tvEmptyPhotos = findViewById(R.id.tv_empty);
        tvEmptyTrips = findViewById(R.id.tv_empty_trips);
        indicatorPhotos = findViewById(R.id.indicator_photos);
        indicatorTrips = findViewById(R.id.indicator_trips);
        bthPhotos = findViewById(R.id.bth_photos);
        btnTrips = findViewById(R.id.btn_trips);

        // Нижняя навигация
        setupBottomNavigation();
    }

    private void checkViews() {
        // Проверка обязательных элементов
        if (rvPhotos == null) {
            throw new RuntimeException("rv_photos не найден! Проверь XML и setContentView");
        }

        if (rvTrips == null) {
            throw new RuntimeException("rv_trips не найден!");
        }

        if (btnFollow == null) {
            Toast.makeText(this, "Кнопка 'Подписаться' не найдена", Toast.LENGTH_SHORT).show();
        }

        if (btnMessage == null) {
            // В XML это ImageButton с ID btn_travel_history, но в коде вы ищете AppCompatButton
            // Исправляем: находим как ImageButton
            btnMessage = findViewById(R.id.btn_travel_history);
            if (btnMessage == null) {
                Toast.makeText(this, "Кнопка 'Сообщение' не найдена", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupAdapters() {
        // Установка LayoutManager (один раз!)
        rvPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvTrips.setLayoutManager(new GridLayoutManager(this, 1));

        // Создание адаптеров с пустыми списками
        rvPhotos.setAdapter(new ImageGridAdapterKs(this, new ArrayList<>()));
        rvTrips.setAdapter(new TripsAdapterKs(this, new ArrayList<>()));
    }

    private void setupClickListeners() {
        // Обработка подписки/отписки
        btnFollow.setOnClickListener(v -> handleFollowClick());



        // Обработка кнопки "Отзывы"
        btnReviews.setOnClickListener(v -> {
            // Открываем активность отзывов или поездок
            startActivity(new Intent(this, CreateTripActivityKs.class));
        });

        // Переключение между фото и поездками
        bthPhotos.setOnClickListener(v -> showPhotos());
        btnTrips.setOnClickListener(v -> showTrips());
    }

    private void setupBottomNavigation() {
        // Находим все кнопки навигации
        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navChat = findViewById(R.id.nav_chat);
        ImageButton navHeart = findViewById(R.id.nav_heart);
        ImageButton navTranslate = findViewById(R.id.nav_translate);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        // Устанавливаем обработчики
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, MainActivity.class));
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                Toast.makeText(this, "Чаты", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, ChatActivity.class));
            });
        }

        if (navHeart != null) {
            navHeart.setOnClickListener(v -> {
                Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, FavoritesActivity.class));
            });
        }

        if (navTranslate != null) {
            navTranslate.setOnClickListener(v -> {
                Toast.makeText(this, "Перевод", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, TranslateActivity.class));
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                Toast.makeText(this, "Профиль", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(this, MyProfileActivityKs.class));
            });
        }
    }

    private void showMenu() {
        // Показываем меню или уведомления
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
        // Можно реализовать всплывающее меню
    }


    private void handleFollowClick() {
        if (!isFollowing) {
            // Подписка
            isFollowing = true;
            btnFollow.setText("Друзья");
            btnFollow.setBackgroundResource(R.drawable.clicked_button_ks);
            btnFollow.setTextColor(Color.parseColor("#435986"));
            Toast.makeText(this, "Вы подписались на пользователя", Toast.LENGTH_SHORT).show();
        } else {
            // Диалог отписки
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
                    .setTitle("Обратите внимание!")
                    .setMessage("Вы уверены, что хотите отписаться?")
                    .setPositiveButton("Да", null)
                    .setNegativeButton("Нет", null);

            AlertDialog dialog = builder.create();

            // Создаём белый скругленный фон для диалога
            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.WHITE);
            background.setCornerRadius(getResources().getDisplayMetrics().density * 12);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(background);
            }

            dialog.setOnShowListener(d -> {
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positive != null) {
                    positive.setBackgroundResource(R.drawable.dialog_btn_bg_ks);
                    positive.setTextColor(Color.parseColor("#435986"));
                    positive.setPadding(40, 20, 40, 20);

                    positive.setOnClickListener(v -> {
                        isFollowing = false;
                        btnFollow.setText("Подписаться");
                        btnFollow.setBackgroundResource(R.drawable.button_ks);
                        btnFollow.setTextColor(Color.WHITE);
                        Toast.makeText(this, "Вы отписались", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (negative != null) {
                    negative.setBackgroundResource(R.drawable.dialog_btn_bg_ks);
                    negative.setTextColor(Color.parseColor("#435986"));
                    negative.setPadding(40, 20, 40, 20);

                    negative.setOnClickListener(v -> dialog.dismiss());
                }
            });

            dialog.show();
        }
    }

    private void loadData() {
        // Загрузка данных фото
        photoList = samplePhotoList();

        // Загрузка данных поездок
        tripList = sampleTripList();

    }



    private void updateEmptyStates() {
        if (tvEmptyPhotos != null) {
            tvEmptyPhotos.setVisibility(photoList.isEmpty() ? View.VISIBLE : View.GONE);
        }

        if (tvEmptyTrips != null) {
            tvEmptyTrips.setVisibility(tripList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void showPhotos() {
        // Обновление индикаторов
        if (indicatorPhotos != null) {
            indicatorPhotos.setVisibility(View.VISIBLE);
        }
        if (indicatorTrips != null) {
            indicatorTrips.setVisibility(View.GONE);
        }

        // Показать фото
        if (rvPhotos != null) {
            rvPhotos.setVisibility(View.VISIBLE);
        }

        // Скрыть поездки
        if (rvTrips != null) {
            rvTrips.setVisibility(View.GONE);
        }
    }

    private void showTrips() {
        // Обновление индикаторов
        if (indicatorPhotos != null) {
            indicatorPhotos.setVisibility(View.GONE);
        }
        if (indicatorTrips != null) {
            indicatorTrips.setVisibility(View.VISIBLE);
        }

        // Показать поездки
        if (rvTrips != null) {
            rvTrips.setVisibility(View.VISIBLE);
        }

        // Скрыть фото
        if (rvPhotos != null) {
            rvPhotos.setVisibility(View.GONE);
        }
    }

    private List<Integer> samplePhotoList() {
        List<Integer> list = new ArrayList<>();
        // Добавьте реальные ресурсы изображений
        // list.add(R.drawable.photo1);
        // list.add(R.drawable.photo2);
        return list;
    }

    private List<TripKs> sampleTripList() {
        List<TripKs> list = new ArrayList<>();
        list.add(new TripKs("Париж", "01.01-05.01", "1000$", "Экскурсионный", "Описание поездки"));
        list.add(new TripKs("Барселона", "10.02-15.02", "1200$", "Пляжный", "Описание поездки"));
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновить данные при возвращении на экран
        loadData();
    }
}