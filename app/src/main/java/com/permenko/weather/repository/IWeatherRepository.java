package com.permenko.weather.repository;

import android.support.annotation.NonNull;

import com.permenko.weather.model.City;

import java.util.ArrayList;

import rx.Observable;

public interface IWeatherRepository {

    @NonNull
    Observable<City> getWeather(@NonNull String cityName);

    @NonNull
    Observable<ArrayList<City>> getGroupWeather();

    @NonNull
    Observable<ArrayList<City>> addCity(@NonNull City city);

    @NonNull
    Observable<ArrayList<City>> deleteCity(int position, @NonNull City city);

}
