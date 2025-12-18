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

import okhttp3.ResponseBody;
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
        String emailStr = email.getText().toString().trim();
        String nameStr = name.getText().toString().trim();
        String surnameStr = surname.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String sexStr = gender.getText().toString().trim();
        String aboutStr = about.getText().toString().trim();

        if (nameStr.isEmpty() || surnameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty()) {
            toast("Заполните обязательные поля");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            toast("Неверный формат email");
            return;
        }

        String birthInput = birthdate.getText().toString().trim();
        String birthFormatted;
        try {
            Date date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(birthInput);
            birthFormatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        } catch (Exception e) {
            toast("Неверный формат даты: дд.MM.гггг");
            return;
        }


        ApiClient.serverApi.register(new RegisterRequest(nameStr, surnameStr, emailStr, passwordStr, sexStr, birthFormatted, aboutStr))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
