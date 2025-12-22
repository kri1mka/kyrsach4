package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class FiltersActivity extends AppCompatActivity {

    private Spinner spinnerGender, spinnerCountry, spinnerTripType;
    private EditText editAgeFrom, editAgeTo;
    private EditText editPriceFrom, editPriceTo;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);

        spinnerTripType = findViewById(R.id.spinnerTripType);
        editAgeFrom = findViewById(R.id.editAgeFrom);
        editAgeTo = findViewById(R.id.editAgeTo);
        btnApply = findViewById(R.id.btnApplyFilters);
        editPriceFrom = findViewById(R.id.editPriceFrom);
        editPriceTo = findViewById(R.id.editPriceTo);



        // ---------- Пол ----------
        String[] genders = {"Любой", "Мужской", "Женский"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genders
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // ---------- Страна ----------
        String[] countries = {"Любая", "Барселона", "Рим", "Париж"};
        spinnerCountry.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                countries
        ));



        // ---------- Тип поездки ----------
        String[] tripTypes = {"Любой", "Тур", "Круиз", "Экскурсия"};
        spinnerTripType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                tripTypes
        ));

        btnApply.setOnClickListener(v -> applyFilters());
    }

    private void applyFilters() {
        Intent intent = new Intent();

        // ---------- ПОЛ ----------
        String genderDb = null;
        if (spinnerGender != null && spinnerGender.getSelectedItem() != null) {
            String genderUi = spinnerGender.getSelectedItem().toString();
            if ("Мужской".equals(genderUi)) {
                genderDb = "Муж"; // ДОЛЖНО совпадать с БД
            } else if ("Женский".equals(genderUi)) {
                genderDb = "Жен";
            }
        }
        if (genderDb != null) {
            intent.putExtra("gender", genderDb);
        }

        // ---------- Страна ----------
        if (spinnerCountry != null && spinnerCountry.getSelectedItem() != null) {
            String country = spinnerCountry.getSelectedItem().toString();
            if (!"Любая".equals(country)) {
                intent.putExtra("country", country);
            }
        }



        // ---------- Тип поездки ----------
        if (spinnerTripType != null && spinnerTripType.getSelectedItem() != null) {
            String tripType = spinnerTripType.getSelectedItem().toString();
            if (!"Любой".equals(tripType)) {
                intent.putExtra("tripType", tripType);
            }
        }

        // ---------- Возраст ----------
        try {
            if (editAgeFrom != null && editAgeFrom.getText() != null) {
                String ageFromStr = editAgeFrom.getText().toString().trim();
                if (!ageFromStr.isEmpty()) {
                    intent.putExtra("ageFrom", Integer.parseInt(ageFromStr));
                }
            }

            if (editAgeTo != null && editAgeTo.getText() != null) {
                String ageToStr = editAgeTo.getText().toString().trim();
                if (!ageToStr.isEmpty()) {
                    intent.putExtra("ageTo", Integer.parseInt(ageToStr));
                }
            }
        } catch (NumberFormatException e) {
            // Если введено не число — игнорируем
            e.printStackTrace();
        }
// ---------- Цена ----------
        try {
            if (editPriceFrom != null && editPriceFrom.getText() != null) {
                String priceFromStr = editPriceFrom.getText().toString().trim();
                if (!priceFromStr.isEmpty()) {
                    // Передаем как double
                    intent.putExtra("priceFrom", Double.parseDouble(priceFromStr));
                }
            }

            if (editPriceTo != null && editPriceTo.getText() != null) {
                String priceToStr = editPriceTo.getText().toString().trim();
                if (!priceToStr.isEmpty()) {
                    // Передаем как double
                    intent.putExtra("priceTo", Double.parseDouble(priceToStr));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        // ---------- Отправка результата ----------
        setResult(RESULT_OK, intent);
        finish();

    }


}
