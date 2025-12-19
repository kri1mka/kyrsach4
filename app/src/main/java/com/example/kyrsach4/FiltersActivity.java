package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FiltersActivity extends AppCompatActivity {

    private RadioGroup radioGender;
    private Spinner spinnerCountry, spinnerDirection, spinnerTripType;
    private EditText editAgeFrom, editAgeTo;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        radioGender = findViewById(R.id.radioGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerDirection = findViewById(R.id.spinnerDirection);
        spinnerTripType = findViewById(R.id.spinnerTripType);
        editAgeFrom = findViewById(R.id.editAgeFrom);
        editAgeTo = findViewById(R.id.editAgeTo);
        btnApply = findViewById(R.id.btnApplyFilters);

        String[] countries = {"Любая", "France", "Japan", "Italy"};
        String[] directions = {"Любое", "Romantic", "Adventure", "Business"};
        String[] tripTypes = {"Любой", "Tour", "Cruise", "Excursion"};

        spinnerCountry.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries));
        spinnerDirection.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, directions));
        spinnerTripType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tripTypes));

        btnApply.setOnClickListener(v -> applyFilters());
    }

    private void applyFilters() {
        // Пол (может быть пустым)
        String gender = "";
        int checkedId = radioGender.getCheckedRadioButtonId();
        if (checkedId == R.id.radioMale) {
            gender = "Man";
        } else if (checkedId == R.id.radioFemale) {
            gender = "Woman";
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
