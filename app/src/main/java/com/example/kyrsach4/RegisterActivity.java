package com.example.kyrsach4;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, gender, birthdate, about;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password_reg);
        gender = findViewById(R.id.gender);
        birthdate = findViewById(R.id.birthdate);
        about = findViewById(R.id.about);
        btnNext = findViewById(R.id.btn_next);
    }
}