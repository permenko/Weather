package com.permenko.weather.view.cities;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.permenko.weather.model.City;

import java.util.List;

public class CitiesDiffCallback extends DiffUtil.Callback {

    private List<City> mOldCities;
    private List<City> mNewCities;

    public CitiesDiffCallback(@NonNull List<City> oldCities, @NonNull List<City> newCities) {
        mOldCities = oldCities;
        mNewCities = newCities;
    }

    @Override
    public int getOldListSize() {
        return mOldCities.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCities.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (int) mOldCities.get(oldItemPosition).getId() == mNewCities.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCities.get(oldItemPosition).equals(mNewCities.get(newItemPosition));
    }
}