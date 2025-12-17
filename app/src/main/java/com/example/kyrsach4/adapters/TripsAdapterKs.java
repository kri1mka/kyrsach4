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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TripsAdapterKs extends RecyclerView.Adapter<TripsAdapterKs.ViewHolder> {


    private List<TripCard> tripList;

    public TripsAdapterKs(List<TripCard> tripList) {
        this.tripList = tripList;
    }

    public void updateData(List<TripCard> newTripList) {
        this.tripList = newTripList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripCard trip = tripList.get(position);

        holder.tvTitle.setText(
                trip.getLocation() != null ? trip.getLocation() : "Без названия"
        );

        holder.tvType.setText(
                trip.getType() != null ? trip.getType() : "Не указан"
        );

        holder.tvDescription.setText(
                trip.getDescription() != null ? trip.getDescription() : "Нет описания"
        );

        holder.tvPrice.setText(
                trip.getPrice() != null
                        ? String.format(Locale.getDefault(), "%.2f$", trip.getPrice())
                        : "0.00$"
        );

        if (trip.getStartDate() != null && trip.getEndDate() != null) {
            try {
                SimpleDateFormat serverFormat =
                        new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat displayFormat =
                        new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

                String start = displayFormat.format(
                        serverFormat.parse(trip.getStartDate())
                );
                String end = displayFormat.format(
                        serverFormat.parse(trip.getEndDate())
                );

                holder.tvDates.setText(start + " - " + end);
            } catch (Exception e) {
                holder.tvDates.setText("Даты не указаны");
            }
        } else {
            holder.tvDates.setText("Даты не указаны");
        }

        // ✅ ФОТО
        if (trip.getPhotoIt() != null && !trip.getPhotoIt().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(trip.getPhotoIt())
                    .placeholder(R.drawable.sample_photo1)
                    .into(holder.ivTripImage);
        } else {
            holder.ivTripImage.setImageResource(R.drawable.sample_photo1);
        }
    }

    @Override
    public int getItemCount() {
        return tripList != null ? tripList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDates, tvPrice, tvType, tvDescription;
        ImageView ivTripImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_trip_title);
            tvDates = itemView.findViewById(R.id.tv_trip_dates);
            tvPrice = itemView.findViewById(R.id.tv_trip_price);
            tvType = itemView.findViewById(R.id.tv_trip_type);
            tvDescription = itemView.findViewById(R.id.tv_trip_description);
            ivTripImage = itemView.findViewById(R.id.iv_trip_image);
        }
    }
}
