package com.permenko.weather.api;

import com.permenko.weather.model.Cities;
import com.permenko.weather.model.City;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {

    @GET("weather")
    Observable<City> getWeather(@Query("appid") String appId,
                                @Query("lang") String language,
                                @Query("units") String units,
                                @Query("q") String cityName);

    @GET("group")
    Observable<Cities> getGroupWeather(@Query("appid") String appId,
                                       @Query("lang") String language,
                                       @Query("units") String units,
                                       @Query("id") String ids);
}