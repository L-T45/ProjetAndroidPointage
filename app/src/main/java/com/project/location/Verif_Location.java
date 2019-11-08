package com.project.location;

import android.util.Log;

public class Verif_Location {
    Localisation current_location;

    public Verif_Location() {
        Log.w("Lat : ", "" + this.current_location.getLat());
        Log.w("Lon : ", "" + this.current_location.getLon());
    }
}
