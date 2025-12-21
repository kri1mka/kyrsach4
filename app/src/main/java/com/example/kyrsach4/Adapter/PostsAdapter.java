package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Post;
import com.example.kyrsach4.network.ApiClient;
import com.example.kyrsach4.network.SessionStorage;
import com.example.kyrsach4.reqresp.LikeRequest;
import com.example.kyrsach4.reqresp.LikeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        //СОСТОЯНИЕ ЛАЙКА
        if (p.is_liked) {
            h.btnLike.setColorFilter(
                    ContextCompat.getColor(context, R.color.like_red)
            );
            h.btnLike.setEnabled(false);
        } else {
            h.btnLike.setColorFilter(
                    ContextCompat.getColor(context, R.color.like_gray)
            );
            h.btnLike.setEnabled(true);
        }

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

        //КЛИК ПО ЛАЙКУ
        h.btnLike.setOnClickListener(v -> {

            h.btnLike.setEnabled(false);//чтобы больше нельзя было кликать

            ApiClient.serverApi.addLike(
                    new LikeRequest(p.id, SessionStorage.userId)
            ).enqueue(new Callback<LikeResponse>() {

                @Override
                public void onResponse(Call<LikeResponse> call,
                                       Response<LikeResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        LikeResponse body = response.body();

                        p.is_liked = body.is_liked;
                        p.likes_count = body.likesCount;

                        h.likesCount.setText(String.valueOf(p.likes_count));
                        h.btnLike.setColorFilter(
                                ContextCompat.getColor(context, R.color.like_red)
                        );
                        h.btnLike.setEnabled(false);

                    } else {
                        h.btnLike.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<LikeResponse> call, Throwable t) {
                    h.btnLike.setEnabled(true);
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, avatar;
        TextView user, description, location, date, likesCount;
        ImageButton btnLike;

        ViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.postImage);
            user = v.findViewById(R.id.postUser);
            description = v.findViewById(R.id.postDescription);
            location = v.findViewById(R.id.postLocation);
            date = v.findViewById(R.id.postDate);
            avatar = v.findViewById(R.id.postAvatar);
            likesCount = v.findViewById(R.id.likes_count);
            btnLike = v.findViewById(R.id.btnLike);

        }
    }
}
