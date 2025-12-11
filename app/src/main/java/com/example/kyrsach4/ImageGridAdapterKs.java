package com.example.kyrsach4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ImageGridAdapterKs extends RecyclerView.Adapter<ImageGridAdapterKs.ViewHolder> {

    private final List<Integer> items;
    private final LayoutInflater inflater;

    public ImageGridAdapterKs(Context context, List<Integer> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ImageGridAdapterKs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_grid_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGridAdapterKs.ViewHolder holder, int position) {
        int resId = items.get(position);
        holder.image.setImageResource(resId);
        holder.image.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
        }
    }
}