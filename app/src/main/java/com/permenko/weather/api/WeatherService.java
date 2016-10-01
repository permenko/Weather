package com.permenko.weather.api;

import com.permenko.weather.model.Cities;
import com.permenko.weather.model.City;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WeatherService {

    @GET("weather")
    Observable<City> getWeather(@Query("q") String cityName);

    @GET("group")
    Observable<Cities> getGroupWeather(@Query("id") String ids);
}