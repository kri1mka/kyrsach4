package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kyrsach4.entity.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> messages;

    // Интерфейс для клика
    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        // Имя и фамилия
        holder.nameText.setText(message.getFirstName() + " " + message.getLastName());

        // Дата/время
        holder.timeText.setText(message.getCreatedAt().toString());

        // Аватар
        String avatarUrl = "http://10.0.2.2:8080/Backend/avatar?file=" + message.getAvatarUrl();

        // Загружаем картинку с Glide
        Glide.with(context)
                .load(avatarUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDE", "Ошибка загрузки: " + e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("GLIDE", "Картинка загружена");
                        return false;
                    }
                })
                .into(holder.avatar);

        // Обработчик клика
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView timeText;
        ImageView avatar;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            timeText = itemView.findViewById(R.id.timeText);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }

    public void updateList(List<Message> newList) {
        messages = newList;
        notifyDataSetChanged();
    }
}
