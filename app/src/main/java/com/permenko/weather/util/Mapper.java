package com.permenko.weather.util;

import com.permenko.weather.model.City;
import com.permenko.weather.model.Main;
import com.permenko.weather.model.Weather;
import com.permenko.weather.model.Wind;
import com.permenko.weather.model.realm.RealmCity;
import com.permenko.weather.model.realm.RealmMain;
import com.permenko.weather.model.realm.RealmWeather;
import com.permenko.weather.model.realm.RealmWind;

import java.util.ArrayList;

import io.realm.RealmList;

//todo: find a better way to map RealmList to ArrayList and back
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

            city.setMain(getMain(realmCity.getMain()));
            city.setId(realmCity.getId());
            city.setName(realmCity.getName());
            city.setWeather(getWeather(realmCity.getWeather()));
            city.setWind(getWind(realmCity.getWind()));

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

            realmCity.setMain(getRealmMain(city.getMain()));
            realmCity.setId(city.getId());
            realmCity.setName(city.getName());
            realmCity.setWeather(getRealmWeather(city.getWeather()));
            realmCity.setWind(getRealmWind(city.getWind()));

            realmCities.add(i, realmCity);
        }
        return realmCities;
    }

    private Main getMain(RealmMain realmMain) {
        Main main = new Main();
        main.setHumidity(realmMain.getHumidity());
        main.setPressure(realmMain.getPressure());
        main.setTemp(realmMain.getTemp());
        main.setTempMax(realmMain.getTempMax());
        main.setTempMin(realmMain.getTempMin());
        return main;
    }

    private RealmMain getRealmMain(Main main) {
        RealmMain realmMain = new RealmMain();
        realmMain.setHumidity(main.getHumidity());
        realmMain.setPressure(main.getPressure());
        realmMain.setTemp(main.getTemp());
        realmMain.setTempMax(main.getTempMax());
        realmMain.setTempMin(main.getTempMin());
        return realmMain;
    }

    private Wind getWind(RealmWind realmWind) {
        Wind wind = new Wind();
        wind.setDeg(realmWind.getDeg());
        wind.setGust(realmWind.getGust());
        wind.setSpeed(realmWind.getSpeed());
        return wind;
    }

    private RealmWind getRealmWind(Wind wind) {
        RealmWind realmWind = new RealmWind();
        realmWind.setDeg(wind.getDeg());
        realmWind.setGust(wind.getGust());
        realmWind.setSpeed(wind.getSpeed());
        return realmWind;
    }

    private ArrayList<Weather> getWeather(RealmList<RealmWeather> weatherRealmList) {
        ArrayList<Weather> weatherList = new ArrayList<>();

        for (int i = 0; i < weatherRealmList.size(); ++i) {
            Weather weather = new Weather();
            RealmWeather realmWeather = weatherRealmList.get(i);
            String description = realmWeather.getDescription();
            weather.setDescription(description);
            weather.setIcon(realmWeather.getIcon());
            weatherList.add(weather);
        }
        return weatherList;
    }

    private RealmList<RealmWeather> getRealmWeather(ArrayList<Weather> weatherList) {
        RealmList<RealmWeather> weatherRealmList = new RealmList<>();
        for (int i = 0; i < weatherList.size(); ++i) {
            RealmWeather realmWeather = new RealmWeather();
            realmWeather.setDescription(weatherList.get(i).getDescription());
            realmWeather.setIcon(weatherList.get(i).getIcon());
            weatherRealmList.add(realmWeather);
        }
        return weatherRealmList;
    }
}