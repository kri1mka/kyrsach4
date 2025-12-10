package com.example.kyrsach4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChangeDataActivityKs extends AppCompatActivity {

    private ImageView buttonBack;
    private ImageView imageProfilePhoto, imageProfileAvatar;
    private TextView textChangePhoto;
    private EditText editName, editSurname, editBirthday, editCityCountry, editTravelType;
    private AppCompatButton buttonSave, buttonCancel;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView selectedImageView;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data_ks);

        // Инициализация элементов
        buttonBack = findViewById(R.id.buttonBack);
        imageProfilePhoto = findViewById(R.id.imageProfilePhoto);
        imageProfileAvatar = findViewById(R.id.imageProfileAvatar);
        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        editBirthday = findViewById(R.id.editBirthday);
        editCityCountry = findViewById(R.id.editCityCountry);
        editTravelType = findViewById(R.id.editTravelType);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Инициализация календаря
        selectedDate = Calendar.getInstance();

        // Настройка лаунчера для выбора фото
        setupImagePicker();

        // Установка обработчиков событий
        setupClickListeners();

        // Загрузка текущих данных пользователя (если есть)
        loadUserData();
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (selectedImageView != null && imageUri != null) {
                            selectedImageView.setImageURI(imageUri);

                            // Можно добавить обработку загрузки на сервер
                            Toast.makeText(this, "Фото изменено", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void setupClickListeners() {
        // Кнопка назад
        buttonBack.setOnClickListener(v -> finish());

        // Выбор фото профиля
        imageProfilePhoto.setOnClickListener(v -> {
            selectedImageView = imageProfilePhoto;
            pickImage();
        });

        // Выбор аватара
        imageProfileAvatar.setOnClickListener(v -> {
            selectedImageView = imageProfileAvatar;
            pickImage();
        });

        // Текст "Изменить фото или аватар"
        textChangePhoto.setOnClickListener(v -> {
            // Можно открыть диалог выбора что изменить
            selectedImageView = imageProfilePhoto; // По умолчанию меняем основное фото
            pickImage();
        });

        // Выбор даты рождения
        editBirthday.setOnClickListener(v -> showDatePicker());

        // Кнопка Сохранить
        buttonSave.setOnClickListener(v -> saveData());

        // Кнопка Отмена
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, dayOfMonth);
                    updateBirthdayText();
                },
                year, month, day
        );

        // Установка максимальной даты (нельзя выбрать будущую дату)
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateBirthdayText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        editBirthday.setText(sdf.format(selectedDate.getTime()));
    }

    private void saveData() {
        // Получение данных из полей
        String name = editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String birthday = editBirthday.getText().toString().trim();
        String cityCountry = editCityCountry.getText().toString().trim();
        String travelType = editTravelType.getText().toString().trim();

        // Валидация данных
        if (name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(this, "Заполните имя и фамилию", Toast.LENGTH_SHORT).show();
            return;
        }

        if (birthday.isEmpty()) {
            Toast.makeText(this, "Выберите дату рождения", Toast.LENGTH_SHORT).show();
            return;
        }

        // Здесь можно добавить сохранение данных:
        // 1. В SharedPreferences для локального хранения
        saveToSharedPreferences(name, surname, birthday, cityCountry, travelType);

        // 2. Отправка на сервер (если есть бэкенд)
        // sendToServer(name, surname, birthday, cityCountry, travelType);

        // Показать сообщение об успехе
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();

        // Возвращаем результат (если нужно)
        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("surname", surname);
        setResult(RESULT_OK, resultIntent);

        // Закрыть активность
        finish();
    }

    private void saveToSharedPreferences(String name, String surname, String birthday,
                                         String cityCountry, String travelType) {
        // Пример сохранения в SharedPreferences
        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .edit()
                .putString("name", name)
                .putString("surname", surname)
                .putString("birthday", birthday)
                .putString("cityCountry", cityCountry)
                .putString("travelType", travelType)
                .apply();
    }

    private void loadUserData() {
        // Загрузка сохраненных данных из SharedPreferences
        String name = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("name", "");
        String surname = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("surname", "");
        String birthday = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("birthday", "");
        String cityCountry = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("cityCountry", "");
        String travelType = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("travelType", "");

        // Установка значений в поля
        editName.setText(name);
        editSurname.setText(surname);
        editBirthday.setText(birthday);
        editCityCountry.setText(cityCountry);
        editTravelType.setText(travelType);

        // Парсинг даты если она есть
        if (!birthday.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate.setTime(sdf.parse(birthday));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Дополнительные методы для работы с сервером
    /*
    private void sendToServer(String name, String surname, String birthday,
                              String cityCountry, String travelType) {
        // Создание объекта пользователя
        UserData userData = new UserData(name, surname, birthday, cityCountry, travelType);

        // Отправка на сервер через Retrofit/Volley
        // ...
    }

    private class UserData {
        private String name;
        private String surname;
        private String birthday;
        private String cityCountry;
        private String travelType;

        public UserData(String name, String surname, String birthday,
                       String cityCountry, String travelType) {
            this.name = name;
            this.surname = surname;
            this.birthday = birthday;
            this.cityCountry = cityCountry;
            this.travelType = travelType;
        }

        // Getters and setters
    }
    */

    @Override
    public void onBackPressed() {
        // Можно добавить проверку на изменения
        if (hasChanges()) {
            // Показать диалог подтверждения выхода
            // ...
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasChanges() {
        // Проверка, были ли изменения в полях
        String currentName = editName.getText().toString().trim();
        String currentSurname = editSurname.getText().toString().trim();
        // ... и т.д.

        // Сравнение с исходными данными
        return !currentName.isEmpty() || !currentSurname.isEmpty();
    }
}