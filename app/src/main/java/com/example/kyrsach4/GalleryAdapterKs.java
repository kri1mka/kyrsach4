package com.example.kyrsach4;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapterKs extends RecyclerView.Adapter<GalleryAdapterKs.ViewHolder> {

    private final List<String> imageUris;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(String imageUri);
    }

    public GalleryAdapterKs(List<String> imageUris, OnImageClickListener listener) {
        this.imageUris = imageUris;
        this.listener = listener;
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
        String imageUri = imageUris.get(position);
        holder.imageView.setImageURI(Uri.parse(imageUri));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick(imageUri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
