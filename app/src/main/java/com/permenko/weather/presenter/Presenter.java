package com.permenko.weather.presenter;

import com.permenko.weather.DbHelper;
import com.permenko.weather.model.ModelImpl;
import com.permenko.weather.model.Model;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Presenter {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private ModelImpl model;
    private DbHelper dbHelper;

    Presenter() {
        model = new ModelImpl();
        dbHelper = new DbHelper();
        model.addDbHelper(dbHelper);
    }

    Model getModel() {
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
