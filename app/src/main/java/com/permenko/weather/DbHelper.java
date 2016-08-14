package com.permenko.weather;

import com.permenko.weather.data.City;
import com.permenko.weather.data.realm.RealmCity;

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

    public void onStop() {
        realm.close();
        mapper = null;
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

    public Observable<ArrayList<City>> cache(ArrayList<City> cities) {
        realm.beginTransaction();

        /*//set updateTime for each City
        for (int i = 0; i < cities.size(); ++i) {
            cities.get(i).setUpdateTime(Utils.getDate());
        }*/
        realm.copyToRealmOrUpdate(mapper.getRealmCities(cities));
        realm.commitTransaction();
        return Observable.just(cities);
    }

    public Observable<ArrayList<City>> getCached() {
        RealmList<RealmCity> realmCities = new RealmList<>();
        //try to get cached data
        realmCities.addAll(realm.where(RealmCity.class).findAll());
        return Observable.just(mapper.getCities(realmCities));
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

    public Observable<ArrayList<City>> addCity(City city) {
        if (contains(city)) return Observable.error(new IllegalArgumentException());
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        return cache(cities);
    }

    public Observable<ArrayList<City>> deleteCity(City city) {
        RealmCity realmCity = realm.where(RealmCity.class).equalTo("id", city.getId()).findFirst();
        realm.beginTransaction();
        realmCity.deleteFromRealm();
        realm.commitTransaction();

        return getCached();
    }

    private boolean isInitialState() {
        return App.isInitialState();
    }
}
