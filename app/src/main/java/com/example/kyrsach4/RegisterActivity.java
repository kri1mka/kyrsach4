package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.AuthApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText name, surname, email, password, gender, birthdate, about;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_reg);
        gender = findViewById(R.id.gender);
        birthdate = findViewById(R.id.birthdate);
        about = findViewById(R.id.about);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> register());
    }

    private void register() {

        String n = name.getText().toString().trim();
        String s = surname.getText().toString().trim();
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();
        String g = gender.getText().toString().trim().toLowerCase();
        String b = birthdate.getText().toString().trim();

        // --- Проверка обязательных полей ---
        if (n.isEmpty() || s.isEmpty() || e.isEmpty() || p.isEmpty()) {
            toast("Заполните обязательные поля");
            return;
        }

        // --- Проверка email ---
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            toast("Неверный формат почты");
            return;
        }

        // --- Проверка пола ---
        if (!(g.equals("woman") || g.equals("man"))) {
            toast("Пол: муж или жен");
            return;
        }

        // --- Проверка даты ---
        if (!isValidDate(b)) {
            toast("Дата в формате дд.ММ.гггг");
            return;
        }

        // --- Запрос к серверу ---
        AuthApi api = ApiClient.retrofit.create(AuthApi.class);

        api.register("register", n, s, e, null, p)
                .enqueue(new Callback<Map<String, Object>>() {

                    @Override
                    public void onResponse(Call<Map<String, Object>> call,
                                           Response<Map<String, Object>> response) {

                        if (response.isSuccessful()) {
                            toast("Регистрация успешна");

                            // 🔁 Переход на экран логина
                            startActivity(new Intent(
                                    RegisterActivity.this,
                                    LoginActivity.class
                            ));
                            finish();
                        } else {
                            toast("Пользователь с такой почтой уже существует");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        toast("Ошибка соединения с сервером");
                    }
                });
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
