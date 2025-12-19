package com.example.kyrsach4.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kyrsach4.R;
import com.example.kyrsach4.entity.TripCard;

import java.util.List;

public class TripsAdapter2 extends RecyclerView.Adapter<TripsAdapter2.TripViewHolder> {

    private List<TripCard> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TripCard trip);
    }

    public TripsAdapter2(List<TripCard> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip2, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, type, description;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_trip_image);
            title = itemView.findViewById(R.id.tv_trip_title);
            type = itemView.findViewById(R.id.tv_trip_type);
            description = itemView.findViewById(R.id.tv_trip_description);
        }

        public void bind(final TripCard trip, final OnItemClickListener listener) {

            title.setText(trip.getLocation());
            type.setText(trip.getType());
            description.setText(trip.getDescription());

            Glide.with(image.getContext())
                    .load(trip.getPhotoIt())
                    .placeholder(R.drawable.sample_photo1)
                    .error(R.drawable.sample_photo1)
                    .into(image);

            itemView.setOnClickListener(v -> listener.onItemClick(trip));
        }
    }
}