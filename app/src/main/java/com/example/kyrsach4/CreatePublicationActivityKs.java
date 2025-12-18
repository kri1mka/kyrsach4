package com.example.kyrsach4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatePublicationActivityKs extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 101;

    private ImageView ivSelected;
    private RecyclerView rvThumbnails;
    private GalleryAdapterKs galleryAdapter;
    private List<String> imagePaths = new ArrayList<>();
    private String lastSelectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_ks);

        initViews();
        checkPermissions();
    }

    private void initViews() {
        // Находим элементы
        ivSelected = findViewById(R.id.iv_selected);
        rvThumbnails = findViewById(R.id.rv_thumbnails);

        // Настройка RecyclerView
        rvThumbnails.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        galleryAdapter = new GalleryAdapterKs(this, imagePaths, this::onImageSelected);
        rvThumbnails.setAdapter(galleryAdapter);

        // Кнопка "Далее"
        findViewById(R.id.tv_next).setOnClickListener(v -> {
            if (lastSelectedImagePath != null) {
                // Здесь можно сохранить выбранное фото или перейти к следующему экрану
                Toast.makeText(this, "Фото выбрано: " + lastSelectedImagePath,
                        Toast.LENGTH_SHORT).show();
                // finish(); // или startActivity(new Intent(...))
            } else {
                Toast.makeText(this, "Выберите фото", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка назад в Toolbar
        findViewById(R.id.topAppBar).setOnClickListener(v -> finish());

        // Также можно сделать кнопку для открытия галереи если нет фото
        if (imagePaths.isEmpty()) {
            Toast.makeText(this, "Загрузка фото...", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13 и выше
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE);
            } else {
                loadGalleryImages();
            }
        } else {
            // Для Android ниже 13
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                loadGalleryImages();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGalleryImages();
            } else {
                Toast.makeText(this, "Разрешение необходимо для загрузки фото",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadGalleryImages() {
        new Thread(() -> {
            List<String> images = getAllImagesFromGallery();

            runOnUiThread(() -> {
                if (images.isEmpty()) {
                    Toast.makeText(this, "Фото не найдены", Toast.LENGTH_SHORT).show();
                    return;
                }

                imagePaths.clear();
                imagePaths.addAll(images);
                galleryAdapter.notifyDataSetChanged();

                // Устанавливаем последнее фото как выбранное
                if (!images.isEmpty()) {
                    lastSelectedImagePath = images.get(0);
                    updateSelectedImage(lastSelectedImagePath);
                }
            });
        }).start();
    }

    private List<String> getAllImagesFromGallery() {
        List<String> imagePaths = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder)) {

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(columnIndex);
                    if (imagePath != null) {
                        imagePaths.add(imagePath);
                    }
                }

                // Переворачиваем список, чтобы самые новые были в начале
                Collections.reverse(imagePaths);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки фото: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        return imagePaths;
    }

    private void onImageSelected(String imagePath) {
        lastSelectedImagePath = imagePath;
        updateSelectedImage(imagePath);
    }

    private void updateSelectedImage(String imagePath) {
        ivSelected.setImageURI(Uri.parse("file://" + imagePath));
    }

    // Альтернативный способ: открыть галерею для выбора фото
    private void openGalleryForSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ivSelected.setImageURI(selectedImageUri);
                // Можно также получить путь к файлу
                String imagePath = getRealPathFromURI(selectedImageUri);
                if (imagePath != null) {
                    lastSelectedImagePath = imagePath;
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = getContentResolver().query(contentUri, projection,
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}