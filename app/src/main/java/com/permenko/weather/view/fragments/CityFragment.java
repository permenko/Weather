package com.permenko.weather.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.data.City;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CityFragment extends Fragment {

    private static final String CITY = "CITY";

    private Unbinder unbinder;

    @BindView(R.id.city_name)
    TextView cityName;
    @BindView(R.id.weather_temperature)
    TextView weatherTemperature;
    @BindView(R.id.weather_humidity)
    TextView weatherHumidity;
    @BindView(R.id.weather_wind_speed)
    TextView weatherWindSpeed;

    public static CityFragment newInstance(City city) {
        CityFragment fragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CITY, city);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_city, container, false);
        unbinder = ButterKnife.bind(this, layout);

        final City city = (City) getArguments().getSerializable(CITY);
        showInfo(city);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void showInfo(City city) {
        String description = "";
        try {
            description = "," + city.getWeather().get(0).getDescription();
        } catch (Exception e) {
            description = "";
        } finally {
            cityName.setText(getString(R.string.city_with_description, city.getName(), description));
        }
        weatherTemperature.setText(getString(R.string.temperature, String.valueOf(city.getMain().getTemp())));
        weatherHumidity.setText(getString(R.string.humidity, String.valueOf(city.getMain().getHumidity())));
        weatherWindSpeed.setText(getString(R.string.wind_speed, String.valueOf(city.getWind().getSpeed())));
    }

}
