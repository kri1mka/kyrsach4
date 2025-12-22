package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.AuthResponse;
import com.example.kyrsach4.reqresp.RegisterRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText name, surname, email, password, birthdate, about;
    RadioGroup genderGroup;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_reg);
        birthdate = findViewById(R.id.birthdate);
        about = findViewById(R.id.about);
        genderGroup = findViewById(R.id.genderGroup);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> register());
    }

    private void register() {
        String nameStr = name.getText().toString().trim();
        String surnameStr = surname.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String aboutStr = about.getText().toString().trim();
        String birthInput = birthdate.getText().toString().trim();

        // Проверка обязательных полей
        if (nameStr.isEmpty() || surnameStr.isEmpty() ||
                emailStr.isEmpty() || passwordStr.isEmpty()) {
            toast("Заполните все обязательные поля");
            return;
        }

        // Email
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            toast("Неверный формат email");
            return;
        }

        // Пароль ()
        if (passwordStr.length() < 6) {
            toast("Пароль должен быть не менее 6 символов");
            return;
        }

        // Пол
        int checkedGenderId = genderGroup.getCheckedRadioButtonId();
        if (checkedGenderId == -1) {
            toast("Выберите пол");
            return;
        }

        String genderStr =
                checkedGenderId == R.id.radioWoman ? "woman" : "man";

        // Дата рождения
        String birthFormatted;
        try {
            SimpleDateFormat input = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            input.setLenient(false);
            Date date = input.parse(birthInput);

            birthFormatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(date);
        } catch (Exception e) {
            toast("Дата рождения должна быть в формате дд.MM.гггг");
            return;
        }

        // Отправка
        ApiClient.serverApi.register(
                new RegisterRequest(
                        nameStr,
                        surnameStr,
                        emailStr,
                        passwordStr,
                        genderStr,
                        birthFormatted,
                        aboutStr
                )
        ).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    toast("Регистрация успешна");
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    toast("Ошибка регистрации: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                toast("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

