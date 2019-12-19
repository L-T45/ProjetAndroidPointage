package com.project.location;

import android.content.Context;
import android.util.Log;

public class Work_Place {

    private double lat_no;
    private double lat_se;
    private double lon_no;
    private double lon_se;
    private Localisation current_location;

    public Work_Place(Context context) {

        this.lat_no = 49.8370388;
        this.lon_no = 3.300165;

        this.lat_se = 49.836805;
        this.lon_se = 3.3004379;



        this.current_location = new Localisation(context);
    }

    public boolean insideZone() {
        return (this.current_location.getLat() >= this.lat_se  && this.current_location.getLat() <= this.lat_no &&
                this.current_location.getLon() >= this.lon_no  && this.current_location.getLon() <= this.lon_se);
    }

    public boolean update_location() {
        current_location.getLocation();
        Log.i("debug","Current position: "+current_location.getLat()+" "+current_location.getLon());
        return insideZone();
    }
}