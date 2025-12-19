package com.example.kyrsach4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.entity.PostCard;
import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.SessionStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostDetailsActivityKs extends AppCompatActivity {

    private ImageView ivPreview;
    private EditText etLocation, etDescription;
    private Button btnPublish;

    private Uri imageUri;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_details_ks);

        ivPreview = findViewById(R.id.iv_preview);
        etLocation = findViewById(R.id.et_location);
        etDescription = findViewById(R.id.et_description);
        btnPublish = findViewById(R.id.btn_publish);

        userId = SessionStorage.userId;
        if (userId == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Получаем выбранное фото
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            Glide.with(this)
                    .load(imageUri)
                    .into(ivPreview);
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        btnPublish.setOnClickListener(v -> uploadPhotoAndCreatePost());
    }

    private void uploadPhotoAndCreatePost() {
        if (imageUri == null) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = createTempFileFromUri(imageUri);
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            ApiClient.serverApi.uploadPostImage(body).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String fileName = response.body().get("fileName");
                        if (fileName != null && !fileName.isEmpty()) {
                            String imageUrl = fileName;
                            createPost(imageUrl);
                        } else {
                            Toast.makeText(CreatePostDetailsActivityKs.this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreatePostDetailsActivityKs.this, "Ошибка сервера при загрузке фото", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    Toast.makeText(CreatePostDetailsActivityKs.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            Toast.makeText(this, "Ошибка чтения файла: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload", ".jpg", getCacheDir());
        try (FileOutputStream os = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        }
        if (is != null) is.close();
        return tempFile;
    }

    private void createPost(String imageUrl) {
        String location = etLocation.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        PostCard post = new PostCard();
        post.setUserId(userId);
        post.setLocation(location);
        post.setDescription(description);
        post.setPhotoIt(imageUrl);

        ApiClient.serverApi.createPost(post).enqueue(new Callback<PostCard>() {
            @Override
            public void onResponse(Call<PostCard> call, Response<PostCard> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreatePostDetailsActivityKs.this, "Публикация создана", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreatePostDetailsActivityKs.this, MyProfileActivityKs.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreatePostDetailsActivityKs.this, "Ошибка создания поста", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostCard> call, Throwable t) {
                Toast.makeText(CreatePostDetailsActivityKs.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
