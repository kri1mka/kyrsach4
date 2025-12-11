package com.example.kyrsach4;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CreateTripActivityKs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ks);

        // Основная кнопка
        Button btnPrimary = findViewById(R.id.btn_primary);
        btnPrimary.setOnClickListener(v -> {
            // Заглушка: просто закрываем
            finish();
        });

        // Ниже кнопки ПУБЛИКАЦИЯ / ПОЕЗДКА
        findViewById(R.id.btn_publication).setOnClickListener(v ->
                finish() // возвращаемся на профиль → там нажмётся Публикация
        );

        findViewById(R.id.btn_trip).setOnClickListener(v -> {
            // ничего — мы уже на поездке
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
