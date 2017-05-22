package com.example.a009.countryselector.RealmModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 009 on 22.05.2017.
 */
@RealmClass
public class CountryModel extends RealmObject {


    private String country;
    private RealmList<CityModel> city;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public RealmList<CityModel> getCity() {
        return city;
    }

    public void setCity(RealmList<CityModel> city) {
        this.city = city;
    }
}
