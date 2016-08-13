package com.permenko.weather.view.adapters;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.data.City;
import com.permenko.weather.view.activities.MainActivity;
import com.permenko.weather.view.dialogs.DeleteCityDialog;
import com.permenko.weather.view.fragments.CitiesFragment;
import com.permenko.weather.view.fragments.CityFragment;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context context;
    private ArrayList<City> objects;
    private CitiesFragment.ActivityListener activityListener;

    public WeatherAdapter(ArrayList<City> objects, CitiesFragment.ActivityListener activityListener) {
        this.objects = objects;
        this.activityListener = activityListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.item_city, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(getItem(position).getName());
        viewHolder.temperature.setText(context.getString(R.string.item_city_temperature, getItem(position).getMain().getTemp().toString()));
        viewHolder.name.setOnClickListener(view -> {
            activityListener.replaceFragment(CityFragment.newInstance(getItem(position)));
            //((MainActivity) context).replaceFragment(CityFragment.newInstance(getItem(position)));
        });
        viewHolder.name.setOnLongClickListener(view -> {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            showDialog(DeleteCityDialog.newInstance(position, getItem(position)));
            return true;
        });
    }

    private City getItem(int position) {
        return objects.get(position);
    }

    public void deleteCity(int position) {
        objects.remove(position);
        notifyItemRemoved(position);
    }

    public void addCity(City city) {
        objects.add(city);
        notifyItemInserted(objects.size());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private void showDialog(DialogFragment dialog) {
        dialog.show(((MainActivity) context).getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView temperature;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.city_name);
            temperature = (TextView) itemView.findViewById(R.id.city_temperature);
        }
    }
}