package com.permenko.weather.model;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class Weather implements Serializable {
    private Integer id;
    private String main;
    private String description;
    private Float speed;
    private Float deg;
    private String icon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    @Nullable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
