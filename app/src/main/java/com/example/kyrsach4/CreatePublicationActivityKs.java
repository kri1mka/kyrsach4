package com.example.kyrsach4;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePublicationActivityKs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_ks);

        // Заглушка «Далее»
        findViewById(R.id.tv_next).setOnClickListener(v -> {
            // Просто закрываем
            finish();
        });

        // Кнопка назад
        ImageButton btnBack = findViewById(R.id.topAppBar);
        btnBack.setOnClickListener(v -> finish());
    }
}
