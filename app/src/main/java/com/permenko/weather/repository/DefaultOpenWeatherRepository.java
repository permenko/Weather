package com.permenko.weather.repository;

import com.permenko.weather.App;
import com.permenko.weather.api.ApiFactory;
import com.permenko.weather.api.WeatherService;
import com.permenko.weather.model.City;
import com.permenko.weather.util.Constants;
import com.permenko.weather.util.DbHelper;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DefaultOpenWeatherRepository implements OpenWeatherRepository, Constants {

    private WeatherService apiInterface = ApiFactory.getWeatherService();
    private DbHelper dbHelper;

    public void addDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Observable<City> getWeather(String cityName) {
        return apiInterface.getWeather(DEFAULT_LANGUAGE, DEFAULT_UNITS, cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<ArrayList<City>> cache(ArrayList<City> cities) {
        return dbHelper.cache(cities);
    }

    private Observable<ArrayList<City>> getCached() {
        return dbHelper.getCached();
    }

    @Override
    public Observable<ArrayList<City>> getGroupWeather() {

        return apiInterface.getGroupWeather(DEFAULT_LANGUAGE, DEFAULT_UNITS, getIds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(cities -> {
                    //check the initial state
                    if (App.isInitialState()) {
                        App.markStateNotInitial();
                    }
                    return cities;
                })
                .flatMap(cities -> Observable.just(cities.getCities()))
                .flatMap(this::cache)
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    return getCached();
                });
    }

    @Override
    public Observable<ArrayList<City>> addCity(City city) {
        return dbHelper.addCity(city);
    }

    @Override
    public Observable<ArrayList<City>> deleteCity(int position, City city) {
        return dbHelper.deleteCity(city);
    }

    private String getIds() {
        return dbHelper.getIds().map(list -> list.toString().replaceAll("[\\[\\]]", "")).toBlocking().first();
    }
}
