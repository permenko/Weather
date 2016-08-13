package com.permenko.weather.view.fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.permenko.weather.R;
import com.permenko.weather.data.City;
import com.permenko.weather.presenter.WeatherPresenter;
import com.permenko.weather.view.adapters.WeatherAdapter;
import com.permenko.weather.view.dialogs.AddCityDialog;
import com.permenko.weather.view.dialogs.CompareCityDialog;
import com.permenko.weather.view.dialogs.ErrorDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CitiesFragment extends Fragment
        implements WeatherPresenter.View {

    private ActivityListener getActivity;

    public interface ActivityListener {
        void replaceFragment(Fragment fragment);
        void showDialog(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            getActivity = (CitiesFragment.ActivityListener) context;
        } catch (ClassCastException e) {
            //Reminder for the developer
            throw new ClassCastException(context.toString()
                    + " must implement ActivityListener");
        }

    }

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
    private ArrayList<City> cities;
    //todo: deal with savedInstanceState and find better way to restore cities data
    private boolean isInitial = true;

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

        if (isInitial) {
            getCities();
            isInitial = false;
        } else if (cities != null) {
            updateAdapter(cities);
        }

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                getActivity.showDialog(new AddCityDialog());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
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
        getActivity.showDialog(ErrorDialog.newInstance(messageId));
    }

    @Override
    public void compareNames(City city) {
        getActivity.showDialog(CompareCityDialog.newInstance(city));
    }

    public void getWeather(String cityName) {
        presenter.getWeather(cityName);
    }

    @Override
    public void updateAdapter(ArrayList<City> cities) {
        this.cities = cities;
        adapter = new WeatherAdapter(cities, getActivity);
        citiesList.setAdapter(adapter);
        citiesList.invalidate();
    }

    public void deleteCity(int position, City city) {
        presenter.deleteCity(position, city);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

}
