package com.permenko.weather;

import com.permenko.weather.data.City;
import com.permenko.weather.data.Weather;
import com.permenko.weather.data.realm.RealmCity;
import com.permenko.weather.data.realm.RealmWeather;

import java.util.ArrayList;

import io.realm.RealmList;

public class Mapper {

    public ArrayList<City> getCities(RealmList<RealmCity> realmCities) {
        if (realmCities == null) {
            return new ArrayList<>();
        }

        ArrayList<City> cities = new ArrayList<>();
        //copy realmCities to cities
        for (int i = 0; i < realmCities.size(); ++i) {
            RealmCity realmCity = realmCities.get(i);
            City city = new City();

            city.setMain(realmCity.getMain());
            city.setId(realmCity.getId());
            city.setName(realmCity.getName());
            city.setWeather(getWeather(realmCity.getWeather()));
            city.setWind(realmCity.getWind());

            cities.add(i, city);
        }
        return cities;
    }

    public RealmList<RealmCity> getRealmCities(ArrayList<City> cities) {
        if (cities == null) {
            return new RealmList<>();
        }

        RealmList<RealmCity> realmCities = new RealmList<>();
        //copy cities to realmCities
        for (int i = 0; i < cities.size(); ++i) {
            City city = cities.get(i);
            RealmCity realmCity = new RealmCity();

            realmCity.setMain(city.getMain());
            realmCity.setId(city.getId());
            realmCity.setName(city.getName());
            realmCity.setWeather(getWeather(city.getWeather()));
            realmCity.setWind(city.getWind());

            realmCities.add(i, realmCity);
        }
        return realmCities;
    }

    private ArrayList<Weather> getWeather(RealmList<RealmWeather> weatherRealmList) {
        ArrayList<Weather> weatherList = new ArrayList<>();

        for (int i = 0; i < weatherRealmList.size(); ++i) {
            Weather weather = new Weather();
            weather.setDescription(weatherRealmList.get(i).getDescription());
            weatherList.add(weather);
        }
        return weatherList;
    }

    private RealmList<RealmWeather> getWeather(ArrayList<Weather> weatherList) {
        RealmList<RealmWeather> weatherRealmList = new RealmList<>();

        for (int i = 0; i < weatherList.size(); ++i) {
            Weather weather = new Weather();
            weather.setDescription(weatherList.get(i).getDescription());
            weatherList.add(weather);
        }
        return weatherRealmList;
    }
}
