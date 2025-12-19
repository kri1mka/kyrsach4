package com.example.kyrsach4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.dto.UpdateProfileRequest;
import com.example.kyrsach4.entity.UserProfile;
import com.example.kyrsach4.network.ApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;

public class ChangeDataActivityKs extends AppCompatActivity {

    private ImageView buttonBack, imageProfilePhoto;
    private TextView textChangePhoto;
    private EditText editName, editSurname, editBirthday, editCityCountry, editTravelType;
    private AppCompatButton buttonSave, buttonCancel;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private Calendar selectedDate;

    private String originalName, originalSurname, originalLocation, originalTravelType, originalPhoto;
    private Integer originalAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data_ks);

        // Инициализация элементов
        buttonBack = findViewById(R.id.buttonBack);
        imageProfilePhoto = findViewById(R.id.imageProfilePhoto);
        textChangePhoto = findViewById(R.id.textChangePhoto);
        editName = findViewById(R.id.editName);
        editSurname = findViewById(R.id.editSurname);
        editBirthday = findViewById(R.id.editBirthday);
        editCityCountry = findViewById(R.id.editCityCountry);
        editTravelType = findViewById(R.id.editTravelType);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        selectedDate = Calendar.getInstance();

        setupImagePicker();
        setupClickListeners();
        loadUserData();
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            selectedImageUri = imageUri;
                            imageProfilePhoto.setImageURI(imageUri);
                            Toast.makeText(this, "Фото изменено", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void setupClickListeners() {
        buttonBack.setOnClickListener(v -> finish());

        imageProfilePhoto.setOnClickListener(v -> pickImage());
        textChangePhoto.setOnClickListener(v -> pickImage());

        editBirthday.setOnClickListener(v -> showDatePicker());

        buttonSave.setOnClickListener(v -> saveData());
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
                    int age = calculateAge(selectedDate);
                    editBirthday.setText(age + " лет");
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private int calculateAge(Calendar birthDate) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    private void saveData() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        boolean hasAnyChanges = false;

        String name = editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String location = editCityCountry.getText().toString().trim();
        String travelType = editTravelType.getText().toString().trim();

        if (!equalsSafe(name, originalName)) {
            request.setName(name);
            hasAnyChanges = true;
        }
        if (!equalsSafe(surname, originalSurname)) {
            request.setSurname(surname);
            hasAnyChanges = true;
        }
        if (!equalsSafe(location, originalLocation)) {
            request.setLocation(location);
            hasAnyChanges = true;
        }
        if (!equalsSafe(travelType, originalTravelType)) {
            request.setTravelType(travelType);
            hasAnyChanges = true;
        }
        if (selectedImageUri != null) {
            request.setPhoto(selectedImageUri.toString());
            hasAnyChanges = true;
        }
        if (selectedDate != null) {
            int age = calculateAge(selectedDate);
            if (originalAge == null || age != originalAge) {
                request.setAge(String.valueOf(age));
                hasAnyChanges = true;
            }
        }

        if (!hasAnyChanges) {
            Toast.makeText(this, "Вы ничего не изменили", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getInt("userId", 1);

        ApiClient.serverApi.updateProfile(userId, request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangeDataActivityKs.this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangeDataActivityKs.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChangeDataActivityKs.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", 1);

        ApiClient.serverApi.getUserProfile(userId).enqueue(new retrofit2.Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile user = response.body();

                    originalName = user.getName();
                    originalSurname = user.getSurname();
                    originalLocation = user.getLocation();
                    originalTravelType = user.getTravelType();
                    originalPhoto = user.getPhoto();
                    originalAge = user.getAge();

                    editName.setText(originalName);
                    ;
                    editSurname.setText(originalSurname);
                    editCityCountry.setText(originalLocation);
                    editTravelType.setText(originalTravelType);
                    if (originalAge != null) {
                        editBirthday.setText(originalAge + " лет");
                        selectedDate = Calendar.getInstance();
                        selectedDate.add(Calendar.YEAR, -originalAge); // приближение
                    }

                    if (originalPhoto != null && !originalPhoto.isEmpty()) {
                        Glide.with(ChangeDataActivityKs.this)
                                .load(originalPhoto)
                                .placeholder(R.drawable.pngtreecat_default_avatar_5416936)
                                .into(imageProfilePhoto);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ChangeDataActivityKs.this, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean equalsSafe(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private boolean hasChanges() {
        return !equalsSafe(editName.getText().toString().trim(), originalName) ||
        !equalsSafe(editSurname.getText().toString().trim(), originalSurname) ||
        !equalsSafe(editCityCountry.getText().toString().trim(), originalLocation) ||
        !equalsSafe(editTravelType.getText().toString().trim(), originalTravelType) ||
        selectedImageUri != null
                || (selectedDate != null && originalAge != null && calculateAge(selectedDate) != originalAge);
    }
}