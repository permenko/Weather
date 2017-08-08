package com.permenko.weather.model;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {
    //private String updateTime;
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Integer id;
    private String name;

    /*public String getUpdateTime() {
        if (updateTime == null || updateTime.isEmpty()) {
            //setUpdateTime(Utils.getDate());
            return "no data";
        }
        return updateTime;
    }

    public City setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }*/

    @Nullable
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
