package com.example.kyrsach4;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.reqresp.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText email, newPassword, repeatPassword;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        newPassword = findViewById(R.id.new_password);
        repeatPassword = findViewById(R.id.repeat_password);
        btnReset = findViewById(R.id.btn_reset);

        btnReset.setOnClickListener(v -> resetPassword());

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish();
        });

    }

    private void resetPassword() {
        String emailStr = email.getText().toString().trim();
        String pass1 = newPassword.getText().toString().trim();
        String pass2 = repeatPassword.getText().toString().trim();

        if (emailStr.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            toast("Заполните все поля");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            toast("Неверный email");
            return;
        }

        if (pass1.length() < 6) {
            toast("Пароль минимум 6 символов");
            return;
        }

        if (!pass1.equals(pass2)) {
            toast("Пароли не совпадают");
            return;
        }

        ApiClient.serverApi.resetPassword(
                new ResetPasswordRequest(emailStr, pass1)
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toast("Пароль успешно изменён");
                    finish(); // возврат на логин
                } else {
                    toast("Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toast("Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

