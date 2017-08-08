package com.permenko.weather.view.cities;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.permenko.weather.R;
import com.permenko.weather.model.City;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<City> mCities;

    private final OnItemClickListener mOnItemClickListener;

    public CitiesAdapter(@NonNull OnItemClickListener onItemClickListener) {
        mCities = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false);
        return new CitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        CitiesViewHolder holder = (CitiesViewHolder) viewHolder;
        City city = getCity(holder.getAdapterPosition());
        holder.bind(city, mOnItemClickListener);
    }

    public void setCities(@NonNull List<City> cities) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CitiesDiffCallback(mCities, cities));
        mCities.clear();
        mCities.addAll(cities);
        diffResult.dispatchUpdatesTo(this);
    }

    private City getCity(int position) {
        return mCities.get(position);
    }

    public void addCity(@NonNull City city) {
        mCities.add(city);
        notifyItemInserted(mCities.size());
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public interface OnItemClickListener {

        void onItemClick(@NonNull City city);

        void onItemLongClick(@NonNull City city);

    }
}