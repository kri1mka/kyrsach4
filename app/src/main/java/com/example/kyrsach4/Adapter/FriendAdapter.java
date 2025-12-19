package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FriendAdapter(Context context, List<Friend> friends) {
        this.context = context;
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);

        // Текст
        holder.name.setText(friend.getFirstName() + " " + friend.getLastName());
        holder.country.setText(friend.getCountry());

        // Аватар
        String avatarUrl = "http://10.0.2.2:8080/Backend/avatar?file=" + friend.getAvatarUrl();
        Glide.with(context)
                .load(avatarUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource
                    ) {
                        Log.e("GLIDE", "Ошибка загрузки", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        return false;
                    }
                })
                .into(holder.avatar);

        // ❌ УБРАЛИ клик со всего itemView
        // holder.itemView.setOnClickListener(...);

        // ✅ КЛИК ТОЛЬКО НА КНОПКЕ
        holder.messageBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name, country;
        Button messageBtn;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            country = itemView.findViewById(R.id.country);
            messageBtn = itemView.findViewById(R.id.messageBtn);
        }
    }

    public void updateList(List<Friend> newList) {
        friends.clear();
        friends.addAll(newList);
        notifyDataSetChanged();
    }
}
