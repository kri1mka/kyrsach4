package com.example.kyrsach4.Adapter;
import android.widget.ImageView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entities.TripCard;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<TripCard> cards;
    private Context context;

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

        if (card.getUser() != null) {
            String fullName = card.getUser().getName() + " " + card.getUser().getSurname();
            if (card.getUser().getInfo() != null) {
                fullName += ", " + card.getUser().getInfo().getAge();
            }
            holder.name.setText(fullName);
        } else {
            holder.name.setText("Unknown");
        }

        holder.dateRange.setText(
                "" +
                        (card.getStartDate() != null ? card.getStartDate() : "") + " - " +
                        (card.getEndDate() != null ? card.getEndDate() : "")
        );

        holder.location.setText("" + (card.getLocation() != null ? card.getLocation() : ""));
        holder.type.setText("" + (card.getType() != null ? card.getType() : ""));
        holder.description.setText(card.getDescription() != null ? card.getDescription() : "");
        holder.price.setText("$" + card.getPrice());

        if (card.getUser() != null &&
                card.getUser().getInfo() != null &&
                card.getUser().getInfo().getAvatarUrl() != null) {

            Glide.with(context)
                    .load(card.getUser().getInfo().getAvatarUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .skipMemoryCache(true) // не использовать кэш памяти
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // не использовать кэш диска
                    .into(holder.image);


        }

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView age, name, location, type, description, dateRange, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_name);
            location = itemView.findViewById(R.id.card_location);
            type = itemView.findViewById(R.id.card_type);
            description = itemView.findViewById(R.id.card_description);
            dateRange = itemView.findViewById(R.id.card_dateRange);
            price = itemView.findViewById(R.id.card_price);
            age = itemView.findViewById(R.id.card_age);
        }
    }

    public void updateList(List<TripCard> newCards) {
        this.cards.clear();
        this.cards.addAll(newCards);
        notifyDataSetChanged();
    }

}
