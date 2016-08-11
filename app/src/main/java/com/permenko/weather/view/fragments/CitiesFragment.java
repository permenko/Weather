package com.permenko.weather.view.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.permenko.weather.R;
import com.permenko.weather.data.City;
import com.permenko.weather.presenter.WeatherPresenter;
import com.permenko.weather.view.adapters.WeatherAdapter;
import com.permenko.weather.view.dialogs.AddCityDialog;
import com.permenko.weather.view.dialogs.CompareCityDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CitiesFragment extends Fragment
        implements WeatherPresenter.View {

    private Unbinder unbinder;
    private View layout;

    private WeatherPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cities_list)
    RecyclerView citiesList;
    @BindView(R.id.progress_bar)
    View progressBar;

    private WeatherAdapter adapter;

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        setRetainInstance(true);
        layout = inflater.inflate(R.layout.fragment_cities, container, false);
        unbinder = ButterKnife.bind(this, layout);
        initToolbar();
        presenter = new WeatherPresenter(this, savedInstanceState);
        citiesList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (savedInstanceState == null) {
            //should update weather from server
            getCities();
        }
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_city:
                showDialog(new AddCityDialog());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setTitleTextColor(getActivity().getColor(R.color.white));
    }

    private void getCities() {
        presenter.getGroupWeather();
    }

    @Override
    public void updateAdapter(City city) {
        adapter.addCity(city);
    }

    @Override
    public void updateAdapter(int position) {
        adapter.deleteCity(position);
    }

    @Override
    public void addCity(City city) {
        presenter.addCity(city);
    }


    @Override
    public void showMessage(int messageId) {
        showMessage(getString(messageId));
    }

    private void showMessage(String message) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton(R.string.action_ok, null)
                .show();
        //center text in dialog
        ((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

    @Override
    public void compareNames(City city) {
        showDialog(CompareCityDialog.newInstance(city));
    }

    private void showDialog(DialogFragment dialog) {
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getSimpleName());
    }

    public void getWeather(String cityName) {
        presenter.getWeather(cityName);
    }

    @Override
    public void updateAdapter(ArrayList<City> cities) {
        adapter = new WeatherAdapter(cities);
        citiesList.setAdapter(adapter);
        citiesList.invalidate();
    }

    public void deleteCity(int position, City city) {
        presenter.deleteCity(position, city);
    }

}
