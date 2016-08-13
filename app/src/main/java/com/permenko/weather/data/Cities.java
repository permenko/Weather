package com.permenko.weather.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Cities implements Serializable {
    @SerializedName("cnt")
    private int count;
    @SerializedName("list")
    private ArrayList<City> cities;

    public int getCount() {
        return count;
    }

    public Cities setCount(int count) {
        this.count = count;
        return this;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public Cities setCities(ArrayList<City> cities) {
        this.cities = cities;
        return this;
    }
}
