package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslatorActivity extends AppCompatActivity {

    EditText inputText;
    TextView outputText;
    TextView langFrom, langTo;
    ImageView swapBtn, nav_home, nav_profile, nav_translate, nav_heart, nav_chat;

    String sourceLang = TranslateLanguage.RUSSIAN;
    String targetLang = TranslateLanguage.ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        langFrom = findViewById(R.id.langFrom);
        langTo = findViewById(R.id.langTo);
        swapBtn = findViewById(R.id.swapBtn);

        nav_home = findViewById(R.id.nav_home);
        nav_profile = findViewById(R.id.nav_profile);
        nav_translate = findViewById(R.id.nav_translate);
        nav_heart = findViewById(R.id.nav_heart);
        nav_chat = findViewById(R.id.nav_chat);

        findViewById(R.id.btnTranslate).setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show();
                return;
            }
            translate(text);
        });

        swapBtn.setOnClickListener(v -> swapLanguages());

        nav_chat.setOnClickListener(v -> {
            // Возвращаемся на ChatActivity !!!!!!!!! вставить когда добавим
            Intent intent = new Intent(TranslatorActivity.this, TranslatorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        nav_heart.setOnClickListener(v -> {
            // Возвращаемся на HeartActivity !!!!!!!!! вставить когда добавим
            Intent intent = new Intent(TranslatorActivity.this, TranslatorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        nav_translate.setOnClickListener(v -> {
            // Возвращаемся на TranslatorActivity
            Intent intent = new Intent(TranslatorActivity.this, TranslatorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        nav_profile.setOnClickListener(v -> {
            // Возвращаемся на MyProfileActivityKs
            Intent intent = new Intent(TranslatorActivity.this, MyProfileActivityKs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        nav_home.setOnClickListener(v -> {
            // Возвращаемся на HomeActivity
            Intent intent = new Intent(TranslatorActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void swapLanguages() {
        String temp = sourceLang;
        sourceLang = targetLang;
        targetLang = temp;

        String fromText = langFrom.getText().toString();
        langFrom.setText(langTo.getText());
        langTo.setText(fromText);

        // Меняем местами текст
        String input = inputText.getText().toString();
        inputText.setText(outputText.getText());
        outputText.setText(input);
    }

    private void translate(String text) {

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang)
                .build();

        Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();


        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused ->
                        translator.translate(text)
                                .addOnSuccessListener(translatedText ->
                                        outputText.setText(translatedText)
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(this,
                                                "Ошибка перевода: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show()
                                )
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Не удалось загрузить модель",
                                Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождаем ресурсы
        Translation.getClient(
                new TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLang)
                        .setTargetLanguage(targetLang)
                        .build()
        ).close();
    }
}
