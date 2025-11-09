package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    ListView helpList;

    String[] titles = {
            "Как зарегистрироваться и создать профиль",
            "Как восстановить доступ к аккаунту",
            "Как настроить уведомления о новых подписчиках",
            "Как управлять настройками приложения",
            "Как найти, очистить и отключить историю просмотра",
            "Как скрыть или удалить профиль"
    };

    String[] pdfFiles = {
            "registration.pdf",
            "restore_access.pdf",
            "notifications.pdf",
            "settings.pdf",
            "history.pdf",
            "hide_profile.pdf"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpList = findViewById(R.id.helpList);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);

        helpList.setAdapter(adapter);

        helpList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(HelpActivity.this, PdfActivity.class);
            intent.putExtra("PDF_NAME", pdfFiles[position]);
            startActivity(intent);
        });
    }
}
