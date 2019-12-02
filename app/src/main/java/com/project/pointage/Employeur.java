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

import com.project.pointage.ui.login.LoginActivity;

public class Employeur extends AppCompatActivity {

    private SharedPreferences preferences ;
    private boolean status = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeur);
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(intent.hasExtra("user") && !user.isEmpty() ){
            Message messenger = new Message();
            messenger.message(Employeur.this,null,"Bienvenu "+user,2);
        }
    }





    //NE PAS TOUCHER A CETTTE FONCTION
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Veuillez confirmer votre déconnexion");
        builder.setNegativeButton("NON",null);
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String user = preferences.getString("user",null);
                Log.i("debug","The user :"+user);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user",null);
                editor.putInt("fonction",-1);
                editor.apply();
                Employeur.super.onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
