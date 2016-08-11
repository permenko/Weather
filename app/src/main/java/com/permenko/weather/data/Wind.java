package com.permenko.weather.data;

import java.io.Serializable;

import io.realm.RealmObject;

public class Wind extends RealmObject implements Serializable {

    private Float speed;
    private Float deg;
    private Float gust;

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDeg() {
        return deg;
    }

    public void setDeg(Float deg) {
        this.deg = deg;
    }

    public Float getGust() {
        return gust;
    }

    public void setGust(Float gust) {
        this.gust = gust;
    }
}
