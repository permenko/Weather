package com.permenko.weather.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Cities implements Serializable {
    @SerializedName("cnt")
    private int count;
    @SerializedName("list")
    private List<City> cities;

    public int getCount() {
        return count;
    }

    public Cities setCount(int count) {
        this.count = count;
        return this;
    }

    public List<City> getCities() {
        return cities;
    }

    public Cities setCities(List<City> cities) {
        this.cities = cities;
        return this;
    }
}
