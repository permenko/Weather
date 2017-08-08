package com.permenko.weather.repository;

import android.support.annotation.NonNull;

import com.permenko.weather.model.City;

import java.util.List;

import rx.Observable;

public interface IWeatherRepository {

    @NonNull
    Observable<City> getWeather(@NonNull String cityName);

    @NonNull
    Observable<List<City>> getGroupWeather();

    @NonNull
    Observable<City> addCity(@NonNull City city);

    @NonNull
    Observable<List<City>> deleteCity(@NonNull City city);

}
