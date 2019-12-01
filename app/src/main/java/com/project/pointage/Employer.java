package com.project.pointage;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.prefs.Preferences;

public class Employer extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Message messenger = new Message();
        messenger.message(Employer.this,null,"Bienvenu "+prefs.getString("user",null),2);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Veuillez confirmer votre déconnexion");
        builder.setNegativeButton("NON",null);
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String user = pref.getString("user",null);
                Log.i("debug","The user :"+user);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user",null);
                editor.apply();
                Employer.super.onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
