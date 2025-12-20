package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final List<Post> posts;
    private final Context context;
    private final OnAvatarClickListener avatarClickListener;

    public PostsAdapter(Context context,
                        List<Post> posts,
                        OnAvatarClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.avatarClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(int userId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Post p = posts.get(position);

        h.user.setText(p.userName);
        h.description.setText(p.description);
        h.location.setText(p.location);
        h.date.setText(p.createdAt);
        h.likesCount.setText(String.valueOf(p.likes_count));

        Glide.with(context)
                .load(p.photoUrl)
                .placeholder(R.drawable.placeholder)
                .into(h.image);

        Glide.with(context)
                .load(p.avatarUrl)
                .placeholder(R.drawable.placeholder)
                .into(h.avatar);

        h.avatar.setOnClickListener(v -> {
            if (avatarClickListener != null) {
                avatarClickListener.onAvatarClick(p.user_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, avatar;
        TextView user, description, location, date, likesCount;

        ViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.postImage);
            user = v.findViewById(R.id.postUser);
            description = v.findViewById(R.id.postDescription);
            location = v.findViewById(R.id.postLocation);
            date = v.findViewById(R.id.postDate);
            avatar = v.findViewById(R.id.postAvatar);
            likesCount = v.findViewById(R.id.likes_count);
        }
    }
}
