package com.example.kyrsach4;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kyrsach4.network.SessionStorage;

import java.util.ArrayList;
import java.util.List;

public class CreatePublicationActivityKs extends AppCompatActivity {

    private ImageView ivSelected;
    private RecyclerView rvThumbnails;
    private GalleryAdapterKs galleryAdapter;
    private final List<String> imageUris = new ArrayList<>();
    private String lastSelectedImageUri;

    private static final int PERMISSION_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_ks);

        if (SessionStorage.userId == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ivSelected = findViewById(R.id.iv_selected);
        rvThumbnails = findViewById(R.id.rv_thumbnails);

        rvThumbnails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryAdapter = new GalleryAdapterKs(imageUris, this::onImageSelected);
        rvThumbnails.setAdapter(galleryAdapter);

        findViewById(R.id.topAppBar).setOnClickListener(v -> finish());

        findViewById(R.id.btn_trip).setOnClickListener(v -> {
            startActivity(new Intent(CreatePublicationActivityKs.this, CreateTripActivityKs.class));
        });

        findViewById(R.id.tv_next).setOnClickListener(v -> {
            if (lastSelectedImageUri == null) {
                Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CreatePublicationActivityKs.this, CreatePostDetailsActivityKs.class);
            intent.putExtra("image_uri", lastSelectedImageUri);
            startActivity(intent);
        });

        requestGalleryPermission();
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{android.Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST
            );
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            loadImagesFromGallery();
        }
    }

    private void loadImagesFromGallery() {
        new Thread(() -> {
            List<String> images = getImagesFromMediaStore();
            runOnUiThread(() -> {
                if (images.isEmpty()) {
                    Toast.makeText(this, "Фотографии не найдены", Toast.LENGTH_LONG).show();
                    return;
                }

                imageUris.clear();
                imageUris.addAll(images);
                galleryAdapter.notifyDataSetChanged();

                lastSelectedImageUri = images.get(0);
                updateSelectedImage(lastSelectedImageUri);
            });
        }).start();
    }

    private List<String> getImagesFromMediaStore() {
        List<String> images = new ArrayList<>();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Images.Media._ID};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(collection, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    );
                    images.add(contentUri.toString());
                }
            }
        }
        return images;
    }

    private void onImageSelected(String imageUri) {
        lastSelectedImageUri = imageUri;
        updateSelectedImage(imageUri);
    }

    private void updateSelectedImage(String imageUri) {
        ivSelected.setImageURI(Uri.parse(imageUri));
    }
}
