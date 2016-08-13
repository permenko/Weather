package com.permenko.weather.model;

import com.permenko.weather.data.City;

import java.util.ArrayList;

import rx.Observable;

public interface Model {

    Observable<City> getWeather(String cityName);
    Observable<ArrayList<City>> getGroupWeather();
    Observable<ArrayList<City>> addCity(City city);
    Observable<ArrayList<City>> deleteCity(int position, City city);

}
