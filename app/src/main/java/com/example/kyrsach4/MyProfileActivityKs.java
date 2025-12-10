package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivityKs extends AppCompatActivity {

    private RecyclerView rvPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pfofile_ks);

        // Кнопка назад
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Кнопка “Изменить” → создаёт публикацию
        MaterialButton btnMessage = findViewById(R.id.btn_message);
        btnMessage.setOnClickListener(v ->
                startActivity(new Intent(this, CreatePublicationActivityKs.class))
        );

        // Кнопка “История поездок”
        MaterialButton btnReviews = findViewById(R.id.btn_reviews);
        btnReviews.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTripActivityKs.class))
        );

        // Сетка фото
        rvPhotos = findViewById(R.id.rv_photos);
        rvPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvPhotos.setAdapter(new ImageGridAdapterKs(this, sampleImageList()));
    }

    private List<Integer> sampleImageList() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.a);
        list.add(R.drawable.a);
        list.add(R.drawable.a);
        list.add(R.drawable.a);
        list.add(R.drawable.a);
        list.add(R.drawable.a);
        return list;
    }
}
