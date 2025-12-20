package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.AuthResponse;
import com.example.kyrsach4.network.SessionStorage;
import com.example.kyrsach4.reqresp.LoginRequest;
import com.example.kyrsach4.reqresp.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    TextView textRegister, forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email_phone);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        textRegister = findViewById(R.id.no_account);
        forgotPass = findViewById(R.id.forgot_pass);

        btnLogin.setOnClickListener(v -> validateLogin());

        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPass.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

    }

    private void validateLogin() {
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();

        if (emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Введите email и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.serverApi.login(new LoginRequest(emailStr, passwordStr))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if ("ok".equals(response.body().status)) {

                                SessionStorage.userId = response.body().userId;
                                Toast.makeText(LoginActivity.this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }

                            else {
                                Toast.makeText(
                                        LoginActivity.this,
                                        response.body().error,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } else {
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Ошибка входа: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }



                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


}
