package com.permenko.weather.repository;

import com.permenko.weather.model.City;

import java.util.ArrayList;

import rx.Observable;

public interface OpenWeatherRepository {

    Observable<City> getWeather(String cityName);

    Observable<ArrayList<City>> getGroupWeather();

    Observable<ArrayList<City>> addCity(City city);

    Observable<ArrayList<City>> deleteCity(int position, City city);

}
