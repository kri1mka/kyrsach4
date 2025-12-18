package com.example.kyrsach4;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class GalleryAdapterKs extends RecyclerView.Adapter<GalleryAdapterKs.ViewHolder> {

    private final List<String> imagePaths;
    private final OnImageClickListener listener;
    private final CreatePublicationActivityKs context;

    public interface OnImageClickListener {
        void onImageClick(String imagePath);
    }

    public GalleryAdapterKs(CreatePublicationActivityKs context,
                            List<String> imagePaths,
                            OnImageClickListener listener) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.drawable.item_gallery_thumbnail_ks, parent, false);
        return new ViewHolder(view);
    }

    // В GalleryAdapterKs.onBindViewHolder():
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);

        // Простой способ без Glide
        holder.imageView.setImageURI(Uri.fromFile(new File(imagePath)));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageClick(imagePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public void updateData(List<String> newImagePaths) {
        imagePaths.clear();
        imagePaths.addAll(newImagePaths);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}