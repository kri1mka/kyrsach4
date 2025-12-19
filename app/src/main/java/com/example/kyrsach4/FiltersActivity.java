package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FiltersActivity extends AppCompatActivity {

    private Spinner spinnerGender, spinnerCountry, spinnerDirection, spinnerTripType;
    private EditText editAgeFrom, editAgeTo;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerDirection = findViewById(R.id.spinnerDirection);
        spinnerTripType = findViewById(R.id.spinnerTripType);
        editAgeFrom = findViewById(R.id.editAgeFrom);
        editAgeTo = findViewById(R.id.editAgeTo);
        btnApply = findViewById(R.id.btnApplyFilters);

        // Пол как Spinner
        String[] genders = {"Любой", "Мужской", "Женский"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Остальные Spinner
        String[] countries = {"Любая", "France", "Japan", "Italy"};
        String[] directions = {"Любое", "Romantic", "Adventure", "Business"};
        String[] tripTypes = {"Любой", "Tour", "Cruise", "Excursion"};

        spinnerCountry.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries));
        spinnerDirection.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, directions));
        spinnerTripType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tripTypes));

        btnApply.setOnClickListener(v -> applyFilters());
    }

    private void applyFilters() {
        // Получаем выбранный пол
        String gender = spinnerGender.getSelectedItem().toString();
        if (gender.equals("Любой")) {
            gender = "";
        }

        String country = spinnerCountry.getSelectedItem().toString();
        String direction = spinnerDirection.getSelectedItem().toString();
        String tripType = spinnerTripType.getSelectedItem().toString();

        int ageFrom = -1;
        int ageTo = -1;

        if (!editAgeFrom.getText().toString().trim().isEmpty()) {
            ageFrom = Integer.parseInt(editAgeFrom.getText().toString().trim());
        }
        if (!editAgeTo.getText().toString().trim().isEmpty()) {
            ageTo = Integer.parseInt(editAgeTo.getText().toString().trim());
        }

        Intent intent = new Intent();
        intent.putExtra("gender", gender);
        intent.putExtra("country", country);
        intent.putExtra("direction", direction);
        intent.putExtra("tripType", tripType);
        intent.putExtra("ageFrom", ageFrom);
        intent.putExtra("ageTo", ageTo);

        setResult(RESULT_OK, intent);
        finish();
    }
}
