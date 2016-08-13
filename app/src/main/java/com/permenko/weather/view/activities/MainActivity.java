package com.permenko.weather.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.permenko.weather.R;
import com.permenko.weather.data.City;
import com.permenko.weather.view.dialogs.AddCityDialog;
import com.permenko.weather.view.dialogs.CompareCityDialog;
import com.permenko.weather.view.dialogs.DeleteCityDialog;
import com.permenko.weather.view.fragments.CitiesFragment;

public class MainActivity extends AppCompatActivity
        implements DeleteCityDialog.ClickListener, AddCityDialog.ClickListener, CompareCityDialog.ClickListener, CitiesFragment.ActivityListener {

    private final String CITIES_FRAGMENT_TAG = "CitiesFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            replaceFragment(new CitiesFragment(), CITIES_FRAGMENT_TAG);
        }
    }

    //todo: change somehow
    //using only for CitiesFragment
    private void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void getWeather(String cityName) {
        ((CitiesFragment) getSupportFragmentManager().findFragmentByTag(CITIES_FRAGMENT_TAG))
                .getWeather(cityName);
    }

    @Override
    public void addCity(City city) {
        ((CitiesFragment) getSupportFragmentManager().findFragmentByTag(CITIES_FRAGMENT_TAG))
                .addCity(city);
    }

    @Override
    public void deleteCity(int position, City city) {
        ((CitiesFragment) getSupportFragmentManager().findFragmentByTag(CITIES_FRAGMENT_TAG))
                .deleteCity(position, city);
    }
}
