package com.permenko.weather.presenter;

import com.permenko.weather.repository.DefaultOpenWeatherRepository;
import com.permenko.weather.repository.OpenWeatherRepository;
import com.permenko.weather.util.DbHelper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

//todo: do not use base presenter
public class Presenter {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private DefaultOpenWeatherRepository model;
    private DbHelper dbHelper;

    Presenter() {
        model = new DefaultOpenWeatherRepository();
        dbHelper = new DbHelper();
        model.addDbHelper(dbHelper);
    }

    OpenWeatherRepository getModel() {
        return model;
    }

    void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void getWeather(String cityName) {
    }

    public void getGroupWeather() {
    }

    public void onStop() {
        mCompositeSubscription.clear();
        dbHelper.onStop();
    }
}
