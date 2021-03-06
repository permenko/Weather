package com.permenko.weather.view.cities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.permenko.weather.R;
import com.permenko.weather.model.City;
import com.permenko.weather.repository.WeatherRepository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

//// FIXME: 9/30/16 recreating activity stops current call(request), find a way to restart it (or use loader)
public class CitiesPresenter {

    private final String BUNDLE_CITIES = "BUNDLE_CITIES";

    private CitiesView mCitiesView;
    private WeatherRepository mWeatherRepository;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private List<City> mCities;

    public CitiesPresenter(@NonNull CitiesView view, @NonNull WeatherRepository repository) {
        this.mCitiesView = view;
        this.mWeatherRepository = repository;
    }

    @SuppressWarnings("unchecked")
    public void init(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getGroupWeather();
        } else {
            mCities = ((List<City>) savedInstanceState.getSerializable(BUNDLE_CITIES));
            if (mCities != null) {
                this.mCitiesView.setCitiesToAdapter(mCities);
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
                .subscribe(cityList -> mCitiesView.setCitiesToAdapter(cityList));
        addSubscription(subscription);
    }

    public void getWeather(@NonNull final String cityName) {
        Subscription subscription = mWeatherRepository.getWeather(cityName)
                .doOnSubscribe(() -> mCitiesView.showLoading())
                .doOnTerminate(() -> mCitiesView.hideLoading())
                .subscribe(city -> {
                    if (!city.getName().toLowerCase().equals(cityName.toLowerCase().trim())) {
                        mCitiesView.showCompareCityDialog(city);
                    } else {
                        addCity(city);
                    }
                }, throwable -> mCitiesView.showMessage(getMessageId(throwable)));
        addSubscription(subscription);
    }

    public void onAddClick(@NonNull City city) {
        addCity(city);
    }

    private void addCity(@NonNull City city) {
        Subscription subscription = mWeatherRepository.addCity(city)
                .subscribe(_city -> {
                    mCities.add(_city);
                    mCitiesView.addCityToAdapter(_city);
                }, throwable -> mCitiesView.showMessage(R.string.error_already_added));
        addSubscription(subscription);
    }

    public void onDeleteClick(@NonNull City city) {
        deleteCity(city);
    }

    private void deleteCity(@NonNull City city) {
        Subscription subscription = mWeatherRepository.deleteCity(city)
                .subscribe(
                        cities -> {
                            mCities.clear();
                            mCities.addAll(cities);
                            mCitiesView.setCitiesToAdapter(cities);
                        },
                        throwable -> mCitiesView.showMessage(R.string.error_unknown),
                        () -> {});
        addSubscription(subscription);
    }

    public void onRefreshClick() {
        //getGroupWeather call showLoading, but we don't want to show
        getGroupWeather();
        mCitiesView.hideLoading();
    }

    public void onMenuAddClick() {
        mCitiesView.showAddCityDialog();
    }

    public void onItemClick(@NonNull City city) {
        mCitiesView.openCityScreen(city);
    }

    public void onItemLongClick(@NonNull City city) {
        mCitiesView.showDeleteCityDialog(city);
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
    private List<City> save(@NonNull List<City> cities) {
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
            outState.putSerializable(BUNDLE_CITIES, new ArrayList<>(mCities));
        }
    }
}
