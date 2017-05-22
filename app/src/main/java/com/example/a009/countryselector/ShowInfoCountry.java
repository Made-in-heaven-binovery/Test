package com.example.a009.countryselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.a009.countryselector.Interface.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ShowInfoCountry extends AppCompatActivity {

    private static String TAG = "ShowInfoCountry";
    private JSONObject jsonObject;
    private String summary,title,url;
    private TextView content,city_title,city_url;

    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.geonames.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    API api = retrofit.create(API.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_country);

        content = (TextView) findViewById(R.id.content);
        city_title = (TextView) findViewById(R.id.title);
        city_url = (TextView) findViewById(R.id.url);

        String city = getIntent().getStringExtra("city");
        Call<ResponseBody> call = api.getCityInfo(city,"1","md23e85");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray geoArray = jsonObject.getJSONArray("geonames");
                    JSONObject cityInfoJson = geoArray.optJSONObject(0);
                    summary = cityInfoJson.getString("summary");
                    title = cityInfoJson.getString("title");
                    url = cityInfoJson.getString("wikipediaUrl");

                    content.setText(summary);
                    city_title.setText(title);
                    city_url.setText(url);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,t.toString());
            }
        });

    }


}
