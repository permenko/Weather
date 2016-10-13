package com.permenko.weather.view.cities;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.permenko.weather.model.City;

import java.util.List;

public interface CitiesView {

    void openCityScreen(@NonNull City city);

    void addCityToAdapter(@NonNull City city);

    void deleteCityFromAdapter(int position); //delete city by position

    void addCitiesToAdapter(@NonNull List<City> cities);

    void showCompareCityDialog(@NonNull City city);

    void showAddCityDialog();

    void showDeleteCityDialog(@NonNull City city, int position);

    void showMessage(int messageId);

    void showLoading();

    void hideLoading();

    void showRefreshing();

    void hideRefreshing();

}
