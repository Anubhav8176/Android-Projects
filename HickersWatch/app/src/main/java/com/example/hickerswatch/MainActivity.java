package com.example.hickerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView, latitudeTextView, longitudeTextView, accuracyTextView, altitudeTextView,
            addressTextView;
    ImageView imageView;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude,longitude, Altitude, Accuracy;
    String address;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Accuracy = location.getAccuracy();
                Altitude = location.getAltitude();
                Double roundedLatitude = new BigDecimal(latitude).setScale(2, RoundingMode.HALF_UP).doubleValue();
                Double roundedLongitude = new BigDecimal(longitude).setScale(2, RoundingMode.HALF_UP).doubleValue();
                Double roundedAccuracy = new BigDecimal(Accuracy).setScale(2, RoundingMode.HALF_UP).doubleValue();
                Double roundedAltitude = new BigDecimal(Altitude).setScale(2, RoundingMode.HALF_UP).doubleValue();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, 1);
                    Log.i("Location is:", listAddress.get(0).toString());
                    latitudeTextView.setText("Latitude:\n"+roundedLatitude);
                    longitudeTextView.setText("Longitude:\n"+roundedLongitude);
                    addressTextView.setText("Address:\n"+listAddress.get(0).getAddressLine(0).toString());
                    altitudeTextView.setText("Altitude:\n"+roundedAltitude);
                    accuracyTextView.setText("Accuracy:\n"+roundedAccuracy);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        // Asking user the permission about the accessing of location or not.
        // Also the function will execute what will happen if user allows the permission.
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
                Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

    }
}