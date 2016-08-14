package com.permenko.weather.presenter;

import android.os.Bundle;

import com.google.gson.JsonSyntaxException;
import com.permenko.weather.R;
import com.permenko.weather.data.City;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeatherPresenter extends Presenter {

    private final String BUNDLE_CITIES = "BUNDLE_CITIES";

    private View view;

    private ArrayList<City> cities;

    public interface View {
        void updateAdapter(City city);
        void updateAdapter(int position); //delete city by position
        void addCity(City city);
        void showMessage(int messageId);
        void compareNames(City city);
        void updateAdapter(ArrayList<City> cities);
        void showProgress();
        void hideProgress();
    }

    public WeatherPresenter(View view, Bundle savedInstanceState) {
        this.view = view;

        if (savedInstanceState != null) {
            cities = ((ArrayList<City>) savedInstanceState.getSerializable(BUNDLE_CITIES));
        }

        if (cities != null) {
            this.view.updateAdapter(cities);
        }

    }

    @Override
    public void getWeather(final String cityName) {

        view.showProgress();

        Subscription subscription = getModel().getWeather(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<City>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        int errorId;
                        if (e instanceof UnknownHostException) {
                            errorId = R.string.error_no_internet_connection;
                        } else if (e instanceof TimeoutException) {
                            errorId = R.string.error_timeout;
                        } else if (e instanceof IllegalArgumentException) {
                            errorId = R.string.error_already_added;
                        } else if (e instanceof JsonSyntaxException) { //imagine that the user have no internet connection
                            errorId = R.string.error_json_syntax;
                        } else if (e instanceof NullPointerException) { // means that city was null (i'm almost sure)
                            errorId = R.string.error_city_not_found;
                        } else  {
                            errorId = R.string.error_unknown;
                        }
                        view.showMessage(errorId);
                        view.hideProgress();
                    }

                    @Override
                    public void onNext(City city) {
                        if (!city.getName().toLowerCase().equals(cityName.toLowerCase().trim())) {
                            view.compareNames(city);
                        } else {
                            addCity(city);
                        }
                    }
                });
        addSubscription(subscription);
    }

    public Subscription addCity(City city) {
        return getModel()
                .addCity(city)
                .subscribe(cities -> view.updateAdapter(city), throwable -> view.showMessage(R.string.error_already_added));
    }

    public Subscription deleteCity(int position, City city) {
        return getModel()
                .deleteCity(position, city)
                .subscribe(o -> view.updateAdapter(position), throwable -> view.showMessage(R.string.error_unknown));
    }

    @Override
    public void getGroupWeather() {

        view.showProgress();

        Subscription subscription = getModel().getGroupWeather()
                .doOnNext(cityList -> {
                    cities = cityList;
                    view.updateAdapter(cities);
                    view.hideProgress();
                })
                .subscribe();
        addSubscription(subscription);
    }

    public void onSaveInstanceState(Bundle outState) {

        if (outState == null) {
            return;
        }

        if (cities != null) {
            outState.putSerializable(BUNDLE_CITIES, cities);
        }

    }
}
