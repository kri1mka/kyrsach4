package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RightSwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_swipes);

        ImageButton backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RightSwipeActivity.this, SwipeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
