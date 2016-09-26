package com.permenko.weather.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.view.cities.CitiesFragment;
import com.permenko.weather.view.cities.dialog.AddCityDialog;
import com.permenko.weather.view.cities.dialog.CompareCityDialog;
import com.permenko.weather.view.cities.dialog.DeleteCityDialog;

//todo: use activities instead of fragments
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
                .setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void showDialog(DialogFragment dialog) {
        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
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
