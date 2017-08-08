package com.permenko.weather.repository;

import android.support.annotation.NonNull;

import com.permenko.weather.App;
import com.permenko.weather.api.ApiFactory;
import com.permenko.weather.api.WeatherService;
import com.permenko.weather.model.City;
import com.permenko.weather.util.DbHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherRepository implements IWeatherRepository {

    private WeatherService mWeatherService = ApiFactory.getWeatherService();
    private DbHelper mDbHelper;

    public WeatherRepository(@NonNull DbHelper dbHelper) {
        this.mDbHelper = dbHelper;
    }

    public void release() {
        mDbHelper.release();
    }

    @NonNull
    @Override
    public Observable<City> getWeather(@NonNull String cityName) {
        return mWeatherService.getWeather(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(city -> {
                    if (city == null || city.getName() == null) {
                        //means that we don't found the city
                        //exception handled into presenter
                        throw new NullPointerException();
                    } else {
                        return city;
                    }
                });
    }

    @NonNull
    private Observable<List<City>> cache(@NonNull List<City> cities) {
        return mDbHelper.cache(cities);
    }

    @NonNull
    private Observable<List<City>> getCached() {
        return mDbHelper.getCached();
    }

    @NonNull
    @Override
    public Observable<List<City>> getGroupWeather() {
        return mWeatherService.getGroupWeather(getIds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(cities -> {
                    //check the initial state
                    if (App.isInitialState()) App.markStateNotInitial();
                    return cities;
                })
                .flatMap(cities -> Observable.just(cities.getCities()))
                .flatMap(this::cache)
                .onErrorResumeNext(throwable -> {
                    throwable.printStackTrace();
                    return getCached();
                });
    }

    @NonNull
    @Override
    public Observable<City> addCity(@NonNull City city) {
        return mDbHelper.addCity(city);
    }

    @NonNull
    @Override
    public Observable<List<City>> deleteCity(@NonNull City city) {
        return mDbHelper.deleteCity(city);
    }

    @NonNull
    private String getIds() {
        return mDbHelper.getIds()
                .map(list -> list.toString()
                        .replaceAll("[\\[\\]]", "")
                        .replaceAll(" ", ""))
                .toBlocking().first();
    }
}
