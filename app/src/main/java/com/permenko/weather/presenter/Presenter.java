package com.permenko.weather.presenter;

import com.permenko.weather.DbHelper;
import com.permenko.weather.model.ModelImplRealm;
import com.permenko.weather.model.ModelRealm;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Presenter {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private ModelImplRealm model;
    private DbHelper dbHelper;

    Presenter() {
        model = new ModelImplRealm();
        dbHelper = new DbHelper();
        model.addDbHelper(dbHelper);
    }

    ModelRealm getModel() {
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
