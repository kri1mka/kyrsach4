package com.example.kyrsach4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslatorActivity extends AppCompatActivity {

    EditText inputText;
    TextView outputText;
    Button btnTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        btnTranslate = findViewById(R.id.btnTranslate);

        btnTranslate.setOnClickListener(v -> {
            String text = inputText.getText().toString();
            translateText(text);
        });
    }

    private void translateText(String text) {

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.RUSSIAN)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();

        final Translator translator = Translation.getClient(options);

        translator.downloadModelIfNeeded()
                .addOnSuccessListener(aVoid -> {
                    translator.translate(text)
                            .addOnSuccessListener(translated -> {
                                outputText.setText(translated);
                            });
                });
    }

}
