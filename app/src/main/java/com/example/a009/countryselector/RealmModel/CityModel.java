package com.example.a009.countryselector.RealmModel;

import android.support.annotation.NonNull;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by 009 on 22.05.2017.
 */
@RealmClass
public class CityModel implements RealmModel, Comparable<CityModel>{


    private String city;

    public CityModel() {
    }


    public CityModel(String city) {
        this.city = city;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int compareTo(@NonNull CityModel o) {
        return 0;
    }
}
