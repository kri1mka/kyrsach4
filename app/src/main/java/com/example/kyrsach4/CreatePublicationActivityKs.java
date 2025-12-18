package com.example.kyrsach4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatePublicationActivityKs extends AppCompatActivity {

    private ImageView ivSelected;
    private RecyclerView rvThumbnails;
    private GalleryAdapterKs galleryAdapter;
    private List<String> imagePaths = new ArrayList<>();
    private String lastSelectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_ks);

        ivSelected = findViewById(R.id.iv_selected);
        rvThumbnails = findViewById(R.id.rv_thumbnails);

        rvThumbnails.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        galleryAdapter = new GalleryAdapterKs(imagePaths, this::onImageSelected);
        rvThumbnails.setAdapter(galleryAdapter);

        // Кнопка "назад"
        findViewById(R.id.topAppBar).setOnClickListener(v -> finish());

        // Кнопка "поездка"
        findViewById(R.id.btn_trip).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateTripActivityKs.class));
            finish();
        });

        findViewById(R.id.tv_next).setOnClickListener(v -> {
            if (lastSelectedImagePath == null) {
                Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, CreatePostDetailsActivityKs.class);
            intent.putExtra("image_path", lastSelectedImagePath);
            startActivity(intent);
        });


        loadImagesFromFolder();
    }

    private void loadImagesFromFolder() {
        new Thread(() -> {
            List<String> images = getImagesFromFolder();
            runOnUiThread(() -> {
                if (images.isEmpty()) {
                    Toast.makeText(this, "Фото не найдены. Положите изображения в /sdcard/Pictures/TestImages", Toast.LENGTH_LONG).show();
                    return;
                }

                imagePaths.clear();
                imagePaths.addAll(images);
                galleryAdapter.notifyDataSetChanged();

                // Устанавливаем первую фотографию в большой ImageView
                lastSelectedImagePath = images.get(0);
                updateSelectedImage(lastSelectedImagePath);
            });
        }).start();
    }

    private List<String> getImagesFromFolder() {
        List<String> images = new ArrayList<>();
        File picturesDir = new File(Environment.getExternalStorageDirectory(), "Pictures/TestImages");

        if (!picturesDir.exists() || !picturesDir.isDirectory()) return images;

        File[] files = picturesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().toLowerCase();
                if ((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) && file.isFile()) {
                    images.add(file.getAbsolutePath());
                }
            }
        }

        Collections.reverse(images);
        return images;
    }

    private void onImageSelected(String imagePath) {
        lastSelectedImagePath = imagePath;
        updateSelectedImage(imagePath);
    }

    private void updateSelectedImage(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            ivSelected.setImageURI(Uri.fromFile(file));
        } else {
            Toast.makeText(this, "Файл не найден: " + imagePath, Toast.LENGTH_SHORT).show();
        }
    }

}
