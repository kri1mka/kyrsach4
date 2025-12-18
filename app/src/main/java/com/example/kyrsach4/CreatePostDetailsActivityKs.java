package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.entity.PostCard;
import com.example.kyrsach4.network.ApiClient;

import java.io.File;
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

    private String imagePath;
    private int userId = 1; // потом брать из сессии

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post_details_ks);

        ivPreview = findViewById(R.id.iv_preview);
        etLocation = findViewById(R.id.et_location);
        etDescription = findViewById(R.id.et_description);
        btnPublish = findViewById(R.id.btn_publish);

        imagePath = getIntent().getStringExtra("image_path");

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        if (imagePath == null) {
            finish();
            return;
        }

        // Показываем превью фото
        Glide.with(this)
                .load(new File(imagePath))
                .into(ivPreview);

        btnPublish.setOnClickListener(v -> uploadPhotoAndCreatePost());
    }

    private void uploadPhotoAndCreatePost() {
        File file = new File(imagePath);

        if (!file.exists()) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Загружаем фото на сервер
        ApiClient.api.uploadPostImage(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Сервер возвращает только имя файла
                    String fileName = response.body().get("fileName");
                    if (fileName != null && !fileName.isEmpty()) {
                        // Формируем полный URL
                        String imageUrl = "http://10.0.2.2:8080/Backend/uploads/posts/" + fileName;
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
        post.setPhotoIt(imageUrl); // полный URL

        // Создаем пост на сервере
        ApiClient.api.createPost(post).enqueue(new Callback<PostCard>() {
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
