package com.project.location;

import android.content.Context;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Work_Place {

    private final double latitude;
    private final double longitude;

    private final int radius;

    private float [] distance;

    private Localisation current_location;

    private Context context;

    public Work_Place(Context context) {
        this.latitude = 50.089869;
        this.longitude = 3.439362;

        this.distance = new float[5];
        this.radius = 100;

        this.current_location = new Localisation(context);

        this.context = context;
        current_location.getLocation();
        this.calculDistance();
    }

    public void calculDistance() {
        Location.distanceBetween(this.latitude, this.longitude, this.current_location.getLat(), this.current_location.getLon(), this.distance);
        Log.w("distance", "" + this.distance[0]);
    }

    public boolean insideZone() {
        this.calculDistance();
        if (distance[0] > radius) {

            String distanceValue = "";

            if (distance[0] > 1000) {
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.HALF_UP);
                distanceValue = df.format(distance[0] / 1000) + "km ";
            }
            else {
                distanceValue = (int)distance[0] + "m ";
            }

            Toast.makeText(context, "Vous êtes à " + distanceValue + "de votre lieu de travail", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    public void update_location() {
        current_location.getLocation();
    }
}
