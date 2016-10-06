package com.permenko.weather.view.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.repository.WeatherRepository;
import com.permenko.weather.view.cities.dialog.AddCityDialog;
import com.permenko.weather.view.cities.dialog.CompareCityDialog;
import com.permenko.weather.view.cities.dialog.DeleteCityDialog;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

//// FIXME: 9/30/16 recreating activity stops current call(request), find a way to restart it (or use loader)
public class CitiesPresenter {

    private final String BUNDLE_CITIES = "BUNDLE_CITIES";

    private CitiesView mCitiesView;
    private WeatherRepository mWeatherRepository;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private ArrayList<City> mCities;

    public CitiesPresenter(@NonNull CitiesView view, @NonNull WeatherRepository repository) {
        this.mCitiesView = view;
        this.mWeatherRepository = repository;
    }

    @SuppressWarnings("unchecked")
    public void init(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getGroupWeather();
        } else {
            mCities = ((ArrayList<City>) savedInstanceState.getSerializable(BUNDLE_CITIES));
            if (mCities != null) {
                this.mCitiesView.addCitiesToAdapter(mCities);
            }
        }
    }

    private void getGroupWeather() {
        Subscription subscription = mWeatherRepository.getGroupWeather()
                .doOnSubscribe(() -> mCitiesView.showLoading())
                .doOnTerminate(() -> {
                    //actually we don't need both of them, but we cannot know which one is needed
                    mCitiesView.hideLoading();
                    mCitiesView.hideRefreshing();
                })
                .map(this::save)
                .subscribe(cityList -> mCitiesView.addCitiesToAdapter(cityList));
        addSubscription(subscription);
    }

    public void getWeather(@NonNull final String cityName) {
        Subscription subscription = mWeatherRepository.getWeather(cityName)
                .doOnSubscribe(() -> mCitiesView.showLoading())
                .doOnTerminate(() -> mCitiesView.hideLoading())
                .subscribe(city -> {
                    if (!city.getName().toLowerCase().equals(cityName.toLowerCase().trim())) {
                        mCitiesView.showDialog(CompareCityDialog.newInstance(city));
                    } else {
                        addCity(city);
                    }
                }, throwable -> mCitiesView.showMessage(getMessageId(throwable)));
        addSubscription(subscription);
    }

    public void addCity(@NonNull City city) {
        Subscription subscription = mWeatherRepository.addCity(city)
                .subscribe(cities -> mCitiesView.addCityToAdapter(city), throwable -> mCitiesView.showMessage(R.string.error_already_added));
        addSubscription(subscription);
    }

    public void deleteCity(int position, @NonNull City city) {
        Subscription subscription = mWeatherRepository.deleteCity(position, city)
                .subscribe(o -> mCitiesView.deleteCityFromAdapter(position), throwable -> mCitiesView.showMessage(R.string.error_unknown));
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

    private int getMessageId(@NonNull Throwable throwable) {
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

    @NonNull
    private ArrayList<City> save(@NonNull ArrayList<City> cities) {
        mCities = cities;
        return cities;
    }

    public void addSubscription(@NonNull Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void release() {
        mCompositeSubscription.clear();
        mWeatherRepository.release();
    }

    public void onSaveInstanceState(@Nullable Bundle outState) {

        if (outState == null) {
            return;
        }

        if (mCities != null) {
            outState.putSerializable(BUNDLE_CITIES, mCities);
        }

    }
}
