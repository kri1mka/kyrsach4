package com.example.kyrsach4.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.Message;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private static final int TYPE_LEFT = 0;
    private static final int TYPE_RIGHT = 1;

    private List<Message> messages;
    private int currentUserId;

    public ChatAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getFromUserId() == currentUserId ? TYPE_RIGHT : TYPE_LEFT;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == TYPE_RIGHT ? R.layout.item_message_right : R.layout.item_message_left;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message msg = messages.get(position);
        holder.text.setText(msg.getMessage());
        if (holder.userName != null)
            holder.userName.setText(msg.getName() + " " + msg.getSurname());

        if (holder.avatar != null && msg.getAvatarUrl() != null) {
            Glide.with(holder.avatar.getContext())
                    .load(msg.getAvatarUrl())

                    .into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        TextView userName;
        ImageView avatar;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
            userName = itemView.findViewById(R.id.userName); // ID из layout
            avatar = itemView.findViewById(R.id.avatarImage);     // ID из layout
        }
    }

}