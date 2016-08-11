package com.permenko.weather.model;

import com.permenko.weather.data.City;

import io.realm.RealmList;
import rx.Observable;

public interface ModelRealm {

    Observable<City> getWeather(String cityName);
    Observable<RealmList<City>> getGroupWeather();
    Observable<RealmList<City>> addCity(City city);
    Observable<RealmList<City>> deleteCity(int position, City city);

}
