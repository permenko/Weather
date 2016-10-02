package com.permenko.weather.view.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CityActivity extends AppCompatActivity {

    private static final String KEY_CITY = "CITY";

    public static void start(@NonNull Activity activity, @NonNull City city) {
        Intent intent = new Intent(activity, CityActivity.class);
        intent.putExtra(KEY_CITY, city);
        activity.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weather_temperature)
    TextView mWeatherTemperature;
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.weather_humidity)
    TextView mWeatherHumidity;
    @BindView(R.id.weather_wind_speed)
    TextView mWeatherWindSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        final City city = (City) getIntent().getSerializableExtra(KEY_CITY);
        initToolbar(city.getName());
        showInfo(city);
    }

    private void initToolbar(@NonNull String title) {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(title); //// FIXME: 10/3/16 
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void showInfo(@NonNull City city) {
        //maybe should simplify, not sure
        String icon = city.getWeather() == null || city.getWeather().size() == 0  || city.getWeather().get(0).getIcon() == null ? "" : city.getWeather().get(0).getIcon();

        mWeatherTemperature.setText(getString(R.string.temperature, String.valueOf(Math.round(city.getMain().getTemp()))));
        mWeatherIcon.setImageResource(Utils.getIcon(icon));
        mWeatherHumidity.setText(getString(R.string.humidity, String.valueOf(city.getMain().getHumidity())));
        mWeatherWindSpeed.setText(getString(R.string.wind_speed, String.valueOf(city.getWind().getSpeed())));
    }
}
