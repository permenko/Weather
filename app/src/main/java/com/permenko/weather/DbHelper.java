package com.permenko.weather;

import com.permenko.weather.data.City;

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

    public DbHelper() {
        realm = Realm.getDefaultInstance();
    }

    public void onStop() {
        realm.close();
    }

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

    public Observable<RealmList<City>> cache(RealmList<City> cities) {
        realm.beginTransaction();

        //set updateTime for each City
        for (int i = 0; i < cities.size(); ++i) {
            cities.get(i).setUpdateTime(Utils.getDate());
        }

        realm.copyToRealmOrUpdate(cities);
        realm.commitTransaction();
        return Observable.just(cities);
    }

    public Observable<RealmList<City>> getCached() {
        RealmList<City> cities = new RealmList<>();
        //try to get cached data
        cities.addAll(realm.where(City.class).findAll());
        return Observable.just(cities);
    }

    private boolean contains(final City cityToCheck) {
        return getCached()
                .flatMap(Observable::from)
                .filter(city -> cityToCheck.getId().equals(city.getId()))
                .toList()
                .map(cities -> !cities.isEmpty())
                .toBlocking()
                .first();
    }

    public Observable<RealmList<City>> addCity(City city) {
        if (contains(city)) throw new IllegalArgumentException();
        return cache(new RealmList<>(city));
    }

    public Observable<RealmList<City>> deleteCity(City city) {
        City realmCity = realm.where(City.class).equalTo("id", city.getId()).findFirst();
        realm.beginTransaction();
        realmCity.deleteFromRealm();
        realm.commitTransaction();

        return getCached();
    }

    private boolean isInitialState() {
        return App.isInitialState();
    }
}
