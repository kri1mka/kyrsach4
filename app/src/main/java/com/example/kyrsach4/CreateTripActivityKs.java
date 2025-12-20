package com.example.kyrsach4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.network.SessionStorage;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.kyrsach4.adapters.GalleryAdapterTripKs;
import com.example.kyrsach4.entity.TripCard;
import com.example.kyrsach4.network.ApiClient;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTripActivityKs extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;

    private ShapeableImageView ivBigImage;
    private EditText etWhere, etWhen, etWhen2, etTripType, etPrice, etDescription;
    private RecyclerView rvThumbnails;
    private GalleryAdapterTripKs galleryAdapter;
    private List<String> galleryImages = new ArrayList<>();
    private Uri selectedImageUri;
    private Uri lastSelectedImageUri;
    private ImageView buttonBack;

    private Integer userId;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ks);

        userId = SessionStorage.userId;
        if (userId == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ivBigImage = findViewById(R.id.iv_big_image_container);
        etWhere = findViewById(R.id.et_where);
        etWhen = findViewById(R.id.et_when);
        etWhen2 = findViewById(R.id.et_when2);
        etTripType = findViewById(R.id.et_trip_type);
        etPrice = findViewById(R.id.et_price);
        etDescription = findViewById(R.id.et_description);
        rvThumbnails = findViewById(R.id.rv_thumbnails);
        buttonBack = findViewById(R.id.btn_back);
        buttonBack.setOnClickListener(v -> finish());

        findViewById(R.id.btn_publication).setOnClickListener(v -> {
            startActivity(new Intent(this, CreatePublicationActivityKs.class));
        });

        rvThumbnails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        galleryAdapter = new GalleryAdapterTripKs(galleryImages, path -> {
            lastSelectedImageUri = path.startsWith("http") ? Uri.parse(path) : Uri.fromFile(new File(path));
            Glide.with(this)
                    .load(lastSelectedImageUri)
                    .circleCrop()
                    .placeholder(R.drawable.sample_photo1)
                    .into(ivBigImage);
        }, this);

        rvThumbnails.setAdapter(galleryAdapter);
        loadLocalImages();

        ivBigImage.setOnClickListener(v -> openImagePicker());

        etWhen.setFocusable(false);
        etWhen.setClickable(true);
        etWhen.setOnClickListener(v -> showDatePicker(etWhen, true));

        etWhen2.setFocusable(false);
        etWhen2.setClickable(true);
        etWhen2.setOnClickListener(v -> showDatePicker(etWhen2, false));

        findViewById(R.id.btn_primary).setOnClickListener(v -> {
            if (lastSelectedImageUri != null || selectedImageUri != null) {
                createTripWithImage(lastSelectedImageUri != null ? lastSelectedImageUri : selectedImageUri);
            } else {
                Toast.makeText(this, "Выберите фото поездки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            lastSelectedImageUri = selectedImageUri;
            galleryImages.add(selectedImageUri.toString());
            galleryAdapter.notifyItemInserted(galleryImages.size() - 1);

            Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop()
                    .placeholder(R.drawable.sample_photo1)
                    .into(ivBigImage);
        }
    }

    private void loadLocalImages() {
        File folder = new File(Environment.getExternalStorageDirectory(), "Pictures/TestImages");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().toLowerCase().endsWith(".jpg") ||
                            f.getName().toLowerCase().endsWith(".jpeg") ||
                            f.getName().toLowerCase().endsWith(".png")) {
                        galleryImages.add(f.getAbsolutePath());
                    }
                }
            }
        }
        galleryAdapter.notifyDataSetChanged();
    }

    private void showDatePicker(EditText target, boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);

                    String dateStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    target.setText(dateStr);

                    if (isStartDate) startDate = selected;
                    else endDate = selected;

                    if (!isStartDate && endDate.before(startDate)) {
                        Toast.makeText(this, "Дата окончания не может быть раньше даты начала", Toast.LENGTH_SHORT).show();
                        target.setText("");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        if (isStartDate) picker.getDatePicker().setMinDate(System.currentTimeMillis());
        else picker.getDatePicker().setMinDate(startDate.getTimeInMillis());

        picker.show();
    }

    private void createTripWithImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            String fileName = "trip_" + System.currentTimeMillis() + ".jpg";
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                    "file",
                    fileName,
                    RequestBody.create(bytes, MediaType.parse("image/*"))
            );
            RequestBody location = RequestBody.create(etWhere.getText().toString(), MediaType.parse("text/plain"));
            RequestBody startDateBody = RequestBody.create(etWhen.getText().toString(), MediaType.parse("text/plain"));
            RequestBody endDateBody = RequestBody.create(etWhen2.getText().toString(), MediaType.parse("text/plain"));
            RequestBody type = RequestBody.create(etTripType.getText().toString(), MediaType.parse("text/plain"));
            RequestBody price = RequestBody.create(etPrice.getText().toString().isEmpty() ? "0" : etPrice.getText().toString(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(etDescription.getText().toString(), MediaType.parse("text/plain"));
            RequestBody userIdBody = RequestBody.create(String.valueOf(userId), MediaType.parse("text/plain"));

            ApiClient.serverApi.createTripMultipart(
                    userIdBody, location, startDateBody, endDateBody, type, price, description, imagePart
            ).enqueue(new Callback<TripCard>() {
                @Override
                public void onResponse(Call<TripCard> call, Response<TripCard> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Toast.makeText(CreateTripActivityKs.this,
                                "Поездка создана", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateTripActivityKs.this, MyProfileActivityKs.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(CreateTripActivityKs.this,
                                "Ошибка создания поездки", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TripCard> call, Throwable t) {
                    Toast.makeText(CreateTripActivityKs.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при подготовке фото", Toast.LENGTH_SHORT).show();
        }
    }
}
