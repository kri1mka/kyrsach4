package com.example.kyrsach4.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.PostCard;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
public class PostAdapterKs extends RecyclerView.Adapter<PostAdapterKs.ViewHolder> {
    private static final String BASE_IMAGE_URL =
            "http://10.0.2.2:8080/Backend/images/";

    private List<PostCard> postList;

    public PostAdapterKs(List<PostCard> postList) {
        this.postList = postList;
    }

    public void updateData(List<PostCard> newPosts) {
        this.postList = newPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostCard post = postList.get(position);

        // Имя пользователя
        holder.tvUserName.setText(
                post.getUserName() != null ? post.getUserName() : "Пользователь"
        );

        holder.tvDescription.setText(
                post.getDescription() != null ? post.getDescription() : ""
        );

        holder.tvLocation.setText(
                post.getLocation() != null ? post.getLocation() : ""
        );

        holder.tvLikes.setText(
                String.valueOf(post.getLikesCount() != null ? post.getLikesCount() : 0)
        );

        // ✅ ДАТА (БЕЗ parse!)
        if (post.getCreatedAt() != null) {
            SimpleDateFormat displayFormat =
                    new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            holder.tvDate.setText(displayFormat.format(post.getCreatedAt()));
        } else {
            holder.tvDate.setText("Только что");
        }

        // ✅ ФОТО (ПОЛНЫЙ URL)
        if (post.getPhotoIt() != null && !post.getPhotoIt().isEmpty()) {


            Glide.with(holder.itemView.getContext())
                    .load(post.getPhotoIt())
                    .placeholder(R.drawable.sample_photo1)
                    .into(holder.ivPostImage);
        } else {
            holder.ivPostImage.setImageResource(R.drawable.sample_photo1);
        }
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvDescription, tvLocation, tvLikes, tvDate;
        ImageView ivPostImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.post_username);
            tvDescription = itemView.findViewById(R.id.post_description);
            tvLocation = itemView.findViewById(R.id.post_location);
            tvLikes = itemView.findViewById(R.id.likes_count);
            tvDate = itemView.findViewById(R.id.post_date);
            ivPostImage = itemView.findViewById(R.id.post_image);
        }
    }
}
