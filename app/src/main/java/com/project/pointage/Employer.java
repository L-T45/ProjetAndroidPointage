package com.project.pointage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Employer extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);
        Intent intent = getIntent();
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String user = intent.getStringExtra("user");
        if(intent.hasExtra("user") && !user.isEmpty() ){
            Message messenger = new Message();
            messenger.message(Employer.this,null,"Bienvenu "+user,2);
        }
    }


    //NE PAS TOUCHER A CETTE FONCTION
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Veuillez confirmer votre déconnexion");
        builder.setNegativeButton("NON",null);
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String user = pref.getString("user",null);
                Log.i("debug","The user :"+user);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user",null);
                editor.putInt("fonction",-1);
                editor.apply();
                Employer.super.onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
