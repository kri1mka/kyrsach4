package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Post;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final List<Post> posts;
    private final Context context;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        Post p = posts.get(position);

        h.user.setText(p.userName);
        h.description.setText(p.description);
        h.location.setText(p.location);
        h.date.setText(p.createdAt);

        Glide.with(context)
                .load(p.photoUrl)
                .placeholder(R.drawable.placeholder)
                .into(h.image);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, avatar;
        TextView user, description, location, date;

        ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.postImage);
            user = v.findViewById(R.id.postUser);
            description = v.findViewById(R.id.postDescription);
            location = v.findViewById(R.id.postLocation);
            date = v.findViewById(R.id.postDate);
            avatar = v.findViewById(R.id.postAvatar);
        }
    }
}
