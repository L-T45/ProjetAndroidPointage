package com.project.pointage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.project.location.*;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeCompanies extends AppCompatActivity {

    private SharedPreferences preferences ;
    private boolean doubleBackToExitPressedOnce = false;
    private Work_Place work_place = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        work_place = new Work_Place(EmployeCompanies.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        TextView textView = (TextView) findViewById(R.id.employe);
        textView.setText("Employe: "+preferences.getString("user",null));
        Log.i("debug"," Votre nom: "+preferences.getString("user",null));
        Button sendMessage = (Button) findViewById(R.id.button1);
        sendMessage.setEnabled(true);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("0672038043", null,"L'Employé "+ preferences.getString("user",null)+" vous avertis de son arrivé ou de son départ.Vous pouvez le consulter dans la liste de départ ou d'arrivé des employés sur l'application.", null, null);
                Log.i("debug","Send the message");
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user",null);
            editor.putInt("fonction",-1);
            editor.apply();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}
