package com.permenko.weather.view.city;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CityFragment extends Fragment {

    private static final String CITY = "CITY";

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weather_temperature)
    TextView weatherTemperature;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
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
        initToolbar(city.getName());
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

    private void initToolbar(String title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> activity.getSupportFragmentManager().popBackStack());
    }

    private void showInfo(City city) {
        String icon;
        try {
            icon = city.getWeather().get(0).getIcon();
        } catch (Exception e) {
            icon = "";
        }
        if (icon == null) icon = "";
        weatherTemperature.setText(getString(R.string.temperature, String.valueOf(Math.round(city.getMain().getTemp()))));
        weatherIcon.setImageResource(Utils.getIcon(icon));
        weatherHumidity.setText(getString(R.string.humidity, String.valueOf(city.getMain().getHumidity())));
        weatherWindSpeed.setText(getString(R.string.wind_speed, String.valueOf(city.getWind().getSpeed())));
    }

}
