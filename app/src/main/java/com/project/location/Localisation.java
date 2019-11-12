package com.project.location;

import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class Localisation extends AppCompatActivity {

    private double lat;
    private double lon;
    private Context mContext;
    LocationManager locationManager;
    LocationListener locationListener;



    public Localisation(Context context) {
        this.lat = 0;
        this.lon = 0;
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                   lat = location.getLatitude();
                   lon = location.getLongitude();
                   locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        getLocation();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
        else return true;
    }

    private String getEnabledLocationProvider() {
        if (this.checkPermission()) {



            // Criteria to find location provider.
            Criteria criteria = new Criteria();

            // Returns the name of the provider that best meets the given criteria.
            // ==> "gps", "network",...
            String bestProvider = locationManager.getBestProvider(criteria, true);

            boolean enabled = locationManager.isProviderEnabled(bestProvider);

            if (!enabled) {
                return null;
            }
            Log.w("Best Provider : ", bestProvider);
            return bestProvider;
        }
        else return null;
    }

    private void getLocation() {
        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {
            return;
        }

        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

        android.location.Location myLocation;
        try {
            // This code need permissions (Asked above ***)
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

            // Getting Location.
            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }

        // With Android API >= 23, need to catch SecurityException.
        catch (SecurityException e) {
            return;
        }

        if (myLocation != null) {
            lon = myLocation.getLongitude();
            lat = myLocation.getLatitude();
        }
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }
}
