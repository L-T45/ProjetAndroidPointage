package com.project.pointage;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeCompanies extends AppCompatActivity {

    private SharedPreferences preferences ;
    private boolean doubleBackToExitPressedOnce = false;
    private Work_Place work_place = null;
    private Message message = new Message();
    private ConnectivityManager connectivityManager;
    private Button sendMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);
        work_place = new Work_Place(EmployeCompanies.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        TextView textView = (TextView) findViewById(R.id.employe);
        textView.setText("Employe: "+preferences.getString("user",null));
        Log.i("debug"," Votre nom: "+preferences.getString("user",null));
        connectivityManager = ((ConnectivityManager) EmployeCompanies.this.getSystemService(Context.CONNECTIVITY_SERVICE));
        sendMessage = findViewById(R.id.button1);
        final TextView textView1 = findViewById(R.id.textView2);

        if(work_place.update_location()){
            sendMessage.setText("Envoyer");
            textView1.setVisibility(View.INVISIBLE);
        }else{
            sendMessage.setText("Actualiser");
            textView1.setVisibility(View.VISIBLE);
        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(work_place.insideZone()== false){
                        Log.i("debug","Actualiser votre position");
                        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
                            work_place.update_location();

                        }else{
                         message.message(EmployeCompanies.this,"Connexion Internet","Activer votre connexion internet.",0);
                        }
                }
                else{
                    Log.i("debug","Envoyer votre ");
                    if(ContextCompat.checkSelfPermission(EmployeCompanies.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(EmployeCompanies.this,Manifest.permission.SEND_SMS)){
                            Toast.makeText(EmployeCompanies.this, "Vous devez autoriser l'envoi des sms", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ActivityCompat.requestPermissions(EmployeCompanies.this,new String[]{Manifest.permission.SEND_SMS},2);
                        }
                    }
                    else{
                        Log.i("debug","Autorisé");
                        ActivityCompat.requestPermissions(EmployeCompanies.this,new String[]{Manifest.permission.SEND_SMS},2);

                    }

                }


                if(work_place.update_location()){
                    sendMessage.setText("Envoyer");
                    textView1.setVisibility(View.INVISIBLE);
                }else{
                    sendMessage.setText("Actualiser");
                    textView1.setVisibility(View.VISIBLE);
                }


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
        Toast.makeText(this, "Please click BACK again to disconnect", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==2){
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    SmsManager smsManager = SmsManager.getDefault();

                    SimpleDateFormat format = new SimpleDateFormat("HH");
                    int hour = Integer.parseInt(format.format(new Date()));

                    String msg = "L'employé "+ preferences.getString("user",null)+" vous avertit de son ";
                    msg += (hour < 13) ? "arrivée." : "départ.";

                    smsManager.sendTextMessage("**********", null, msg, null, null);
                    message.message(EmployeCompanies.this,"Confirmation","Votre message a été envoyé.",0);
                }
                else{
                    message.message(EmployeCompanies.this,"Echec","Votre message n'a pas été envoyé.",0);
                }
        }
        else{
                Log.i("debug","Probleme message");
        }

    }
}
