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
import java.util.List;

import io.realm.RealmList;

//todo: find a better way to map RealmList to List and back
public class Mapper {

    public List<City> getCities(RealmList<RealmCity> realmCities) {
        if (realmCities == null) {
            return new ArrayList<>();
        }

        List<City> cities = new ArrayList<>();
        //copy realmCities to cities
        for (int i = 0; i < realmCities.size(); ++i) {
            cities.add(i, getCity(realmCities.get(i)));
        }
        return cities;
    }

    public City getCity(RealmCity realmCity) {
        if (realmCity == null) {
            return new City();
        }
        City city = new City();

        city.setMain(getMain(realmCity.getMain()));
        city.setId(realmCity.getId());
        city.setName(realmCity.getName());
        city.setWeather(getWeather(realmCity.getWeather()));
        city.setWind(getWind(realmCity.getWind()));

        return city;
    }

    public RealmList<RealmCity> getRealmCities(List<City> cities) {
        if (cities == null) {
            return new RealmList<>();
        }

        RealmList<RealmCity> realmCities = new RealmList<>();
        //copy cities to realmCities
        for (int i = 0; i < cities.size(); ++i) {
            realmCities.add(i, getRealmCity(cities.get(i)));
        }
        return realmCities;
    }

    public RealmCity getRealmCity(City city) {
        if (city == null) {
            return new RealmCity();
        }
        RealmCity realmCity = new RealmCity();

        realmCity.setMain(getRealmMain(city.getMain()));
        realmCity.setId(city.getId());
        realmCity.setName(city.getName());
        realmCity.setWeather(getRealmWeather(city.getWeather()));
        realmCity.setWind(getRealmWind(city.getWind()));

        return realmCity;
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

    private List<Weather> getWeather(RealmList<RealmWeather> weatherRealmList) {
        List<Weather> weatherList = new ArrayList<>();

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

    private RealmList<RealmWeather> getRealmWeather(List<Weather> weatherList) {
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
