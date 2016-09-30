package com.permenko.weather.view.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.presenter.Presenter;
import com.permenko.weather.view.cities.dialog.AddCityDialog;
import com.permenko.weather.view.cities.dialog.CompareCityDialog;
import com.permenko.weather.view.cities.dialog.DeleteCityDialog;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//// FIXME: 9/30/16 recreating activity stops current call(request), find a way to restart it (or use loader)
public class CitiesPresenter extends Presenter {

    private final String BUNDLE_CITIES = "BUNDLE_CITIES";

    private CitiesView mCitiesView;

    private ArrayList<City> mCities;

    public CitiesPresenter(CitiesView view) {
        this.mCitiesView = view;
    }

    @SuppressWarnings("unchecked")
    public void init(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getGroupWeather();
        } else {
            mCities = ((ArrayList<City>) savedInstanceState.getSerializable(BUNDLE_CITIES));
        }

        if (mCities != null) {
            this.mCitiesView.addCitiesToAdapter(mCities);
        }
    }

    public void getWeather(final String cityName) {
        Subscription subscription = getRepository().getWeather(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mCitiesView.showLoading())
                .doOnTerminate(() -> mCitiesView.hideLoading())
                .doOnNext(city -> {
                    if (!city.getName().toLowerCase().equals(cityName.toLowerCase().trim())) {
                        mCitiesView.showDialog(CompareCityDialog.newInstance(city));
                    } else {
                        addCity(city);
                    }
                })
                .doOnError(throwable -> mCitiesView.showMessage(getMessageId(throwable)))
                .subscribe();
        addSubscription(subscription);
    }

    public void addCity(City city) {
        Subscription subscription = getRepository().addCity(city)
                .subscribe(cities -> mCitiesView.addCityToAdapter(city), throwable -> mCitiesView.showMessage(R.string.error_already_added));
        addSubscription(subscription);
    }

    public void deleteCity(int position, City city) {
        Subscription subscription = getRepository().deleteCity(position, city)
                .subscribe(o -> mCitiesView.deleteCityFromAdapter(position), throwable -> mCitiesView.showMessage(R.string.error_unknown));
        addSubscription(subscription);
    }

    private void getGroupWeather() {
        Subscription subscription = getRepository().getGroupWeather()
                .doOnSubscribe(() -> mCitiesView.showLoading())
                .doOnTerminate(() -> mCitiesView.hideLoading())
                .map(this::save)
                .subscribe(cityList -> mCitiesView.addCitiesToAdapter(cityList));
        addSubscription(subscription);
    }

    public void onRefresh() {
        //getGroupWeather call showLoading, but we don't want to show
        getGroupWeather();
        mCitiesView.hideLoading();
    }

    public void onMenuAddClick() {
        mCitiesView.showDialog(new AddCityDialog());
    }

    public void onItemClick(@NonNull City city) {
        mCitiesView.openCityScreen(city);
    }

    public void onItemLongClick(@NonNull City city, int position) {
        mCitiesView.showDialog(DeleteCityDialog.newInstance(city, position));
    }

    private int getMessageId(Throwable throwable) {
        int errorId;
        if (throwable instanceof UnknownHostException) {
            errorId = R.string.error_no_internet_connection;
        } else if (throwable instanceof TimeoutException) {
            errorId = R.string.error_timeout;
        } else if (throwable instanceof IllegalArgumentException) {
            errorId = R.string.error_already_added;
        } else if (throwable instanceof JsonSyntaxException) { //imagine that the user have no internet connection
            errorId = R.string.error_json_syntax;
        } else if (throwable instanceof NullPointerException) { // means that city was null (i'm almost sure)
            errorId = R.string.error_city_not_found;
        } else {
            errorId = R.string.error_unknown;
        }
        return errorId;
    }

    private ArrayList<City> save(ArrayList<City> cities) {
        mCities = cities;
        return cities;
    }

    public void onSaveInstanceState(Bundle outState) {

        if (outState == null) {
            return;
        }

        if (mCities != null) {
            outState.putSerializable(BUNDLE_CITIES, mCities);
        }

    }
}
