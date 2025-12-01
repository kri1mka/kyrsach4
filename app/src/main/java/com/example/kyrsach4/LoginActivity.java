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

public class LoginActivity extends AppCompatActivity {

    EditText emailPhone, password;
    Button btnLogin;
    TextView textRegister, forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailPhone = findViewById(R.id.email_phone);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        textRegister = findViewById(R.id.no_account);
        forgotPass = findViewById(R.id.forgot_pass);

        btnLogin.setOnClickListener(v -> validateLogin());

        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPass.setOnClickListener(v ->
                Toast.makeText(this, "Функция восстановления пока недоступна", Toast.LENGTH_SHORT).show()
        );
    }

    private void validateLogin() {
        String login = emailPhone.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(login)) {
            emailPhone.setError("Введите телефон или email");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            password.setError("Введите пароль");
            return;
        }

        if (!login.contains("@") && !login.matches("\\d{10,}")) {
            emailPhone.setError("Введите корректный email или номер телефона");
            return;
        }

        if (pass.length() < 6) {
            password.setError("Минимум 6 символов");
            return;
        }

        Toast.makeText(this, "Вы вошли успешно!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, TranslatorActivity.class);
        startActivity(intent);
        finish();
    }
}
