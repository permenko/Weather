package com.permenko.weather.view.cities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.model.City;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.city_name)
    TextView mName;

    @BindView(R.id.city_temperature)
    TextView mTemperature;

    public CitiesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull City city, @NonNull CitiesAdapter.OnItemClickListener onItemClickListener ) {
        mName.setText(city.getName());

        int temperature = Math.round(city.getMain().getTemp());
        mTemperature.setText(mTemperature.getContext().getString(R.string.item_city_temperature, String.valueOf(temperature)));

        mName.setOnClickListener(view -> onItemClickListener.onItemClick(city));
        mName.setOnLongClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            onItemClickListener.onItemLongClick(city);
            return true;
        });
    }
}
