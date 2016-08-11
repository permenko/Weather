package com.permenko.weather.data;

import com.permenko.weather.Utils;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject implements Serializable {
    private String updateTime;
    //public Coord coord;
    private RealmList<Weather> weather;
    //public String base;
    private Main main;
    //public Integer visibility;
    private Wind wind;
    //public Clouds clouds;
    //public Integer dt;
    //public Sys sys;
    @PrimaryKey
    private Integer id;
    private String name;
    //public Integer cod;

    public String getUpdateTime() {
        if (updateTime == null || updateTime.isEmpty()) {
            //setUpdateTime(Utils.getDate());
            return "no data";
        }
        return updateTime;
    }

    public City setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public RealmList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(RealmList<Weather> weather) {
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
