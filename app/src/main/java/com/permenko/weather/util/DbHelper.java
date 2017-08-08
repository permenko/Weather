package com.permenko.weather.util;

import android.support.annotation.NonNull;

import com.permenko.weather.App;
import com.permenko.weather.model.City;
import com.permenko.weather.model.realm.RealmCity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Observable;

/**
 * Note that Realm tables are unordered.
 * This means that if you want the sorted list
 * you will need to add a field to contain the insertion time.
 */
public class DbHelper {

    private Realm mRealm;
    private Mapper mMapper = null;

    public DbHelper() {
        mRealm = Realm.getDefaultInstance();
        mMapper = new Mapper();
    }

    public void release() {
        mRealm.close();
        mMapper = null;
    }

    @NonNull
    public Observable<List<Integer>> getIds() {
        if (isInitialState()) {
            return Observable.just(Utils.getInitialData());
        } else {
            return getCached()
                    .flatMap(Observable::from)
                    .flatMap(city -> Observable.just(city.getId()))
                    .toList();
        }
    }

    @NonNull
    public Observable<List<City>> cache(@NonNull List<City> cities) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(mMapper.getRealmCities(cities)));
        return Observable.just(cities);
    }

    @NonNull
    public Observable<City> cache(@NonNull City city) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(mMapper.getRealmCity(city)));
        return Observable.just(city);
    }

    @NonNull
    public Observable<List<City>> getCached() {
        RealmList<RealmCity> realmCities = new RealmList<>();
        //try to get cached data
        realmCities.addAll(mRealm.where(RealmCity.class).findAll());
        return Observable.just(mMapper.getCities(realmCities));
    }

    private boolean contains(@NonNull final City cityToCheck) {
        return getCached()
                .flatMap(Observable::from)
                .filter(city -> cityToCheck.getId().equals(city.getId()))
                .toList()
                .map(cities -> !cities.isEmpty())
                .toBlocking()
                .first();
    }

    @NonNull
    public Observable<City> addCity(@NonNull City city) {
        if (contains(city)) return Observable.error(new IllegalArgumentException());
        return cache(city);
    }

    @NonNull
    public Observable<Void> deleteCity(@NonNull City city) {
        mRealm.executeTransaction(realm -> {
            RealmCity realmCity = realm.where(RealmCity.class).equalTo("id", city.getId()).findFirst();
            realmCity.deleteFromRealm();
        });

        return Observable.empty();
    }

    private boolean isInitialState() {
        return App.isInitialState();
    }
}
