package com.permenko.weather.data.realm;

import com.permenko.weather.data.Main;
import com.permenko.weather.data.Wind;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCity extends RealmObject implements Serializable {
    //private String updateTime;
    private RealmList<RealmWeather> weather;
    private Main main;
    private Wind wind;
    @PrimaryKey
    private Integer id;
    private String name;

    /*public String getUpdateTime() {
        if (updateTime == null || updateTime.isEmpty()) {
            //setUpdateTime(Utils.getDate());
            return "no data";
        }
        return updateTime;
    }

    public RealCity setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }*/

    public RealmList<RealmWeather> getWeather() {
        return weather;
    }

    public void setWeather(RealmList<RealmWeather> weather) {
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
