package com.example.kyrsach4.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entities.TripCard;
import com.example.kyrsach4.entities.UserInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<TripCard> cards;
    private Context context;

    private static final String IMAGE_BASE_URL = "http://10.0.2.2:8080/Backend/images/";

    // массив дефолтных аватаров
    private static final int[] DEFAULT_AVATARS = {
            R.drawable.user1,
            R.drawable.user3,
            R.drawable.user2,
            R.drawable.user4,
            R.drawable.user5,
            R.drawable.user6,
            R.drawable.user7
    };

    // индекс текущего дефолтного фото
    private int defaultAvatarIndex = 0;

    public CardAdapter(List<TripCard> cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripCard card = cards.get(position);

        // ---------- Имя и возраст ----------
        if (card.getUser() != null) {
            String fullName = card.getUser().getName() + " " + card.getUser().getSurname();
            if (card.getUserInfo() != null) {
                fullName += ", " + card.getUserInfo().getAge();
            }
            holder.name.setText(fullName);
        } else {
            holder.name.setText("Unknown");
        }

        // ---------- Остальные данные ----------
        holder.location.setText(card.getLocation() != null ? card.getLocation() : "");
        holder.type.setText(card.getType() != null ? card.getType() : "");
        holder.description.setText(card.getDescription() != null ? card.getDescription() : "");
        holder.price.setText("$" + card.getPrice());
        holder.dateRange.setText(
                (card.getStartDate() != null ? card.getStartDate() : "") +
                        " - " +
                        (card.getEndDate() != null ? card.getEndDate() : "")
        );

        // ---------- Фото ----------
        UserInfo userInfo = card.getUserInfo();
        if (userInfo != null && userInfo.getAvatarUrl() != null && !userInfo.getAvatarUrl().isEmpty()) {
            String avatarUrl = IMAGE_BASE_URL + userInfo.getAvatarUrl();
            Glide.with(context)
                    .load(avatarUrl)
                    .circleCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.image);
        } else {
            // циклический выбор дефолтного аватара
            holder.image.setImageResource(DEFAULT_AVATARS[defaultAvatarIndex]);
            defaultAvatarIndex = (defaultAvatarIndex + 1) % DEFAULT_AVATARS.length;
        }
    }

    @Override
    public int getItemCount() {
        return cards != null ? cards.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, location, type, description, dateRange, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_name);
            location = itemView.findViewById(R.id.card_location);
            type = itemView.findViewById(R.id.card_type);
            description = itemView.findViewById(R.id.card_description);
            dateRange = itemView.findViewById(R.id.card_dateRange);
            price = itemView.findViewById(R.id.card_price);
        }
    }

    public void updateList(List<TripCard> newCards) {
        cards.clear();
        cards.addAll(newCards);
        notifyDataSetChanged();
    }
}
