package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Message;

import java.util.List;
import java.util.Locale;

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

        SimpleDateFormat input =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        SimpleDateFormat output =
                new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Date messageDate = input.parse(message.getCreatedAt());

            // Сегодняшняя дата (без времени)
            SimpleDateFormat dayFormat =
                    new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

            String messageDay = dayFormat.format(messageDate);
            String todayDay = dayFormat.format(new Date());

            // Вчера
            Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            String yesterdayDay = dayFormat.format(yesterday);

            if (messageDay.equals(todayDay)) {
                // сегодня → часы:минуты
                holder.timeText.setText(output.format(messageDate));
            } else if (messageDay.equals(yesterdayDay)) {
                // вчера
                holder.timeText.setText("вчера");
            } else {
                // старые сообщения — дата (по желанию)
                SimpleDateFormat dateOnly =
                        new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                holder.timeText.setText(dateOnly.format(messageDate));
            }

        } catch (ParseException e) {
            holder.timeText.setText("");
        }



        // Аватар
        String avatarUrl = "http://10.0.2.2:8080/Backend/avatar?file=" + message.getAvatarUrl();
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
