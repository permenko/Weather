package com.permenko.weather.util;

import android.support.annotation.NonNull;

import com.permenko.weather.App;
import com.permenko.weather.model.City;
import com.permenko.weather.model.realm.RealmCity;

import java.util.ArrayList;
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

    private Realm realm;
    private Mapper mapper = null;

    public DbHelper() {
        realm = Realm.getDefaultInstance();
        mapper = new Mapper();
    }

    public void release() {
        realm.close();
        mapper = null;
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
    public Observable<ArrayList<City>> cache(@NonNull ArrayList<City> cities) {
        realm.executeTransaction(_realm -> _realm.copyToRealmOrUpdate(mapper.getRealmCities(cities)));
        return Observable.just(cities);
    }

    @NonNull
    public Observable<ArrayList<City>> getCached() {
        RealmList<RealmCity> realmCities = new RealmList<>();
        //try to get cached data
        realmCities.addAll(realm.where(RealmCity.class).findAll());
        return Observable.just(mapper.getCities(realmCities));
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
    public Observable<ArrayList<City>> addCity(@NonNull City city) {
        if (contains(city)) return Observable.error(new IllegalArgumentException());
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        return cache(cities);
    }

    @NonNull
    public Observable<ArrayList<City>> deleteCity(@NonNull City city) {
        realm.executeTransaction(_realm -> {
            RealmCity realmCity = _realm.where(RealmCity.class).equalTo("id", city.getId()).findFirst();
            realmCity.deleteFromRealm();
        });

        return getCached();
    }

    private boolean isInitialState() {
        return App.isInitialState();
    }
}
