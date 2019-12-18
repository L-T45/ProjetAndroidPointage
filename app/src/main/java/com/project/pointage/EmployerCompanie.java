package com.project.pointage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.location.*;

public class EmployerCompanie extends AppCompatActivity {


    private Work_Place work_place = null;
    private Message messenger = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        work_place = new Work_Place(EmployerCompanie.this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        messenger = new Message();
        messenger.message(EmployerCompanie.this,null,"Bienvenu "+prefs.getString("user",null),2);
        final TextView textView = (TextView) findViewById(R.id.employe);
        textView.setText("Employe: "+prefs.getString("user",null));
        final Button button = (Button) findViewById(R.id.button1);
        button.setEnabled(true);
        if(work_place.insideZone()){
            button.setEnabled(true);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EmployerCompanie.this, "Hello", Toast.LENGTH_LONG).show();
                SmsManager.getDefault().sendTextMessage("0690916543", null,
                        "Un employé vous avertis de son arrivé ou de son départ." +
                                "Vous pouvez le consulter dans la liste de départ ou d'arrivé des employés sur l'application.",
                        null, null);
        }

    });


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
                EmployerCompanie.super.onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            Log.i("debug","Send the message to my employer");


        }
    }
}
