package com.example.kyrsach4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripsAdapterKs extends RecyclerView.Adapter<TripsAdapterKs.ViewHolder> {
    private final List<TripKs> trips;
    private final LayoutInflater inflater;

    public TripsAdapterKs(Context context, List<TripKs> trips) {
        this.trips = trips;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TripsAdapterKs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_trip_ks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapterKs.ViewHolder holder, int position) {
        TripKs trip = trips.get(position);
        holder.tvPlace.setText(trip.place);
        holder.tvDates.setText(trip.dates);
        holder.tvPrice.setText(trip.price);
        holder.tvType.setText(trip.type);
        holder.tvDescription.setText(trip.description);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlace, tvDates, tvPrice, tvType, tvDescription;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tv_place);
            tvDates = itemView.findViewById(R.id.tv_dates);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvType = itemView.findViewById(R.id.tv_type);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}

