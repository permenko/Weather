package com.permenko.weather.presenter;

import com.permenko.weather.repository.DefaultOpenWeatherRepository;
import com.permenko.weather.repository.OpenWeatherRepository;
import com.permenko.weather.util.DbHelper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

//todo: should move all this stuff to activity and pass to presenter (so we can use fake repo for tests without injection)
public class Presenter {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private DefaultOpenWeatherRepository mRepository;
    private DbHelper dbHelper;

    public Presenter() {
        mRepository = new DefaultOpenWeatherRepository();
        dbHelper = new DbHelper();
        mRepository.addDbHelper(dbHelper);
    }

    public OpenWeatherRepository getRepository() {
        return mRepository;
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void onStop() {
        mCompositeSubscription.clear();
        dbHelper.onStop();
    }
}
