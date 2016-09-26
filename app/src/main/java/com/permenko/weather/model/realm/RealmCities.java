package com.permenko.weather.model.realm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmCities extends RealmObject implements Serializable {
    @SerializedName("cnt")
    private int count;
    @SerializedName("list")
    private RealmList<RealmCity> cities;

    public int getCount() {
        return count;
    }

    public RealmCities setCount(int count) {
        this.count = count;
        return this;
    }

    public RealmList<RealmCity> getCities() {
        return cities;
    }

    public RealmCities setCities(RealmList<RealmCity> cities) {
        this.cities = cities;
        return this;
    }
}
