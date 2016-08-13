package com.permenko.weather.model;

import com.permenko.weather.App;
import com.permenko.weather.DbHelper;
import com.permenko.weather.data.City;
import com.permenko.weather.model.api.ApiInterface;
import com.permenko.weather.model.api.ApiModule;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.permenko.weather.Constants.*;

public class ModelImpl implements Model {

    private ApiInterface apiInterface = ApiModule.getApiInterface(URL);
    private DbHelper dbHelper;

    public void addDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public Observable<City> getWeather(String cityName) {
        return apiInterface.getWeather(API_KEY, DEFAULT_LANGUAGE, DEFAULT_UNITS, cityName)
                .subscribeOn(Schedulers.newThread())
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

        return apiInterface.getGroupWeather(API_KEY, DEFAULT_LANGUAGE, DEFAULT_UNITS, getIds())
                .subscribeOn(Schedulers.newThread())
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
