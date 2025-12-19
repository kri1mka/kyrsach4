package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    ListView helpList;
    ImageButton backBtn;

    String[] questions = {
            "Как защищаются мои персональные данные?",
            "Кто может видеть мой профиль и посты?",
            "Как обезопасить свой аккаунт от взлома?",
            "Что делать при подозрительной активности?",
            "Хранятся ли мои сообщения в безопасности?",
            "Как удалить свои данные из приложения?"
    };

    String[] answers = {
            "Все персональные данные передаются по защищённому соединению (HTTPS) "
                    + "и хранятся в базе данных в зашифрованном виде.",

            "По умолчанию профиль виден только авторизованным пользователям приложения. "
                    + "Личные данные не отображаются публично.",

            "Используйте сложный пароль, не передавайте его третьим лицам "
                    + "и обязательно выходите из аккаунта на чужих устройствах.",

            "Если вы заметили подозрительную активность, немедленно смените пароль "
                    + "и обратитесь в службу поддержки.",

            "Сообщения доступны только участникам переписки и не передаются третьим лицам.",

            "Вы можете удалить профиль в настройках. Все связанные данные будут удалены "
                    + "с сервера без возможности восстановления."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpList = findViewById(R.id.helpList);
        backBtn = findViewById(R.id.backBtn);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questions);
        helpList.setAdapter(adapter);

        helpList.setOnItemClickListener((parent, view, position, id) -> {
            showAnswerDialog(questions[position], answers[position]);
        });

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void showAnswerDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Закрыть", null)
                .show();
    }
}
