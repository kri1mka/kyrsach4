package com.example.kyrsach4.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;

import java.io.File;
import java.util.List;

public class GalleryAdapterTripKs extends RecyclerView.Adapter<GalleryAdapterTripKs.ViewHolder> {

    public interface OnImageClickListener {
        void onImageClick(String pathOrUrl);
    }

    private final List<String> images; // локальные пути или URL
    private final OnImageClickListener listener;
    private final Context context;

    public GalleryAdapterTripKs(List<String> images, OnImageClickListener listener, Context context) {
        this.images = images;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_thumbnail_ks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pathOrUrl = images.get(position);

        // Если путь начинается с http, грузим напрямую
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            Glide.with(context)
                    .load(pathOrUrl)
                    .placeholder(R.drawable.sample_photo1)
                    .centerCrop()
                    .into(holder.thumbnail);
        } else {
            Glide.with(context)
                    .load(new File(pathOrUrl))
                    .placeholder(R.drawable.sample_photo1)
                    .centerCrop()
                    .into(holder.thumbnail);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onImageClick(pathOrUrl);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
