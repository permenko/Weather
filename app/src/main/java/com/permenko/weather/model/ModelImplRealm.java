package com.permenko.weather.model;

import com.permenko.weather.App;
import com.permenko.weather.DbHelper;
import com.permenko.weather.data.City;
import com.permenko.weather.model.api.ApiInterface;
import com.permenko.weather.model.api.ApiModule;

import io.realm.RealmList;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.permenko.weather.Constants.*;

public class ModelImplRealm implements ModelRealm {

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

    private Observable<RealmList<City>> cache(RealmList<City> cities) {
        return dbHelper.cache(cities);
    }

    private Observable<RealmList<City>> getCached() {
        return dbHelper.getCached();
    }

    @Override
    public Observable<RealmList<City>> getGroupWeather() {

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
    public Observable<RealmList<City>> addCity(City city) {
        return dbHelper.addCity(city);
    }

    @Override
    public Observable<RealmList<City>> deleteCity(int position, City city) {
        return dbHelper.deleteCity(city);
    }

    private String getIds() {
        return dbHelper.getIds().map(list -> list.toString().replaceAll("[\\[\\]]", "")).toBlocking().first();
    }
}
