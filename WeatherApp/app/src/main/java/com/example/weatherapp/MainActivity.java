package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView weatherIcon;
    TextView temperatureTextView, humidityTextView, windSpeedText, weatherTextView;
    EditText cityName;

    String temperatureCelsius, humidity, windSpeed, weathertype, weatherIconUrl;

    JSONObject currentWeather;
    JSONObject currentCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherIcon = findViewById(R.id.weatherIcon);
        cityName = findViewById(R.id.cityName);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedText = findViewById(R.id.windSpeedText);
        weatherTextView = findViewById(R.id.weatherTextView);
        progressBar = findViewById(R.id.progressBar);

    }

    public void getData(String city){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonobjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://api.weatherapi.com/v1/current.json?key=2b7a2f92e40c4dc08e173810232603&q="+city+"&aqi=yes",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    //creating the jsonobject to get the information from the api.
                    currentWeather = response.getJSONObject("current");
                    currentCondition = currentWeather.getJSONObject("condition");

                    //Data to be display in the main screen.
                    temperatureCelsius = currentWeather.getString("temp_c")+"°C";
                    humidity = currentWeather.getString("humidity")+"gm/m³";
                    windSpeed = currentWeather.getString("wind_kph")+"km/h";
                    weathertype = currentCondition.getString("text");
                    weatherIconUrl = currentCondition.getString("icon");
                    Log.i("The image url is:", weatherIconUrl);

                    //Adding image according to the weather.
                    Glide.with(MainActivity.this).load(weatherIconUrl).into(weatherIcon);
                    progressBar.setVisibility(View.INVISIBLE);

                    // Displaying all the good stuff
                    Display();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("The Response", "Something went wrong");

            }
        });

        requestQueue.add(jsonobjectRequest);
    }


    public void SeeWeather(View view){
        progressBar.setVisibility(View.VISIBLE);
        String city = cityName.getText().toString();
        getData(city);
    }


    public void Display(){
        temperatureTextView.setText("Temperature: "+temperatureCelsius);
        humidityTextView.setText("Humidity: "+humidity);
        windSpeedText.setText("Wind Speed: "+windSpeed);
        weatherTextView.setText(weathertype);

        temperatureTextView.setVisibility(View.VISIBLE);
        humidityTextView.setVisibility(View.VISIBLE);
        windSpeedText.setVisibility(View.VISIBLE);
        weatherTextView.setVisibility(View.VISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
    }
}