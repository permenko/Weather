package com.permenko.weather.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Cities extends RealmObject implements Serializable {
    @SerializedName("cnt")
    private int count;
    @SerializedName("list")
    private RealmList<City> cities;

    public int getCount() {
        return count;
    }

    public Cities setCount(int count) {
        this.count = count;
        return this;
    }

    public RealmList<City> getCities() {
        return cities;
    }

    public Cities setCities(RealmList<City> cities) {
        this.cities = cities;
        return this;
    }
}
