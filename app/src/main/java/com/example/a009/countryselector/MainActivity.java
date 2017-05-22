package com.example.a009.countryselector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.a009.countryselector.Adapter.CityListAdapter;
import com.example.a009.countryselector.Interface.API;
import com.example.a009.countryselector.RealmModel.CountryModel;
import com.example.a009.countryselector.RealmModel.CityModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private JSONObject jsonObject;
    private MaterialSpinner spinner;
    private ArrayList<String> spinnerList;
    private RecyclerView recyclerView;
    private CityListAdapter adapter;

    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .client(getUnsafeOkHttpClient())
            .baseUrl("https://raw.githubusercontent.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    API api = retrofit.create(API.class);

    RealmConfiguration config;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(null);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        spinnerList = new ArrayList<String>();
        Realm.init(getApplicationContext());
        config = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME).schemaVersion(3).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item != null) {
                    Log.d(TAG, "Pick " + item.toString());
                    if(!realm.isInTransaction()){realm.beginTransaction();}
                    CountryModel query = realm.where(CountryModel.class).equalTo("country", item.toString()).findFirst();
                    adapter = new CityListAdapter(query);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Call<ResponseBody> call = api.getCountry();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray array = jsonObject.names();

                    if(!realm.isInTransaction()){realm.beginTransaction();}
                    for (int i = 0; i < array.length(); i++){
                        CountryModel country = realm.createObject(CountryModel.class);
                        RealmList<CityModel> cityList = new RealmList<CityModel>();
                        country.setCountry(array.getString(i));
                         for (int j = 0; j < jsonObject.getJSONArray(array.getString(i)).length(); j++) {
                             CityModel city = realm.createObject(CityModel.class);
                             city.setCity((String) jsonObject.getJSONArray(array.getString(i)).get(j));
                             cityList.add(city);
                         }
                         country.setCity(cityList);
                    }
                    for (int i = 0; i < realm.where(CountryModel.class).findAll().size(); i++) {
                        spinnerList.add(realm.where(CountryModel.class).findAll().get(i).getCountry());
                    }
                    spinner.setItems(spinnerList);
                    if(realm.isInTransaction()){realm.commitTransaction();}
                    Log.d(TAG, "Loading country list success");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "Loading country list error: "+ t.getMessage());
            }
        });



    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();


            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
