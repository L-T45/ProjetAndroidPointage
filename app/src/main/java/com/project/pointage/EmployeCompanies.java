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
    private TextView textView1;

    private String employeeNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);
        work_place = new Work_Place(EmployeCompanies.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        TextView textView = findViewById(R.id.employe);
        textView.setText("Employe: "+preferences.getString("user",null));
        Log.i("debug"," Votre nom: "+preferences.getString("user",null));
        connectivityManager = ((ConnectivityManager) EmployeCompanies.this.getSystemService(Context.CONNECTIVITY_SERVICE));
        sendMessage = findViewById(R.id.send_sms);
        textView1 = findViewById(R.id.permission_asking);

        employeeNumber = "0668475292";

        sendMessage.setText("Vérifier la position");
        textView1.setVisibility(View.VISIBLE);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sendMessage.getText() == "Vérifier la position") {

                    if (ActivityCompat.checkSelfPermission(EmployeCompanies.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (EmployeCompanies.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(EmployeCompanies.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    else {
                        position();
                    }

                }

                else{
                    Log.i("debug","Envoyer votre ");
                    if(ContextCompat.checkSelfPermission(EmployeCompanies.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EmployeCompanies.this, new String[]{Manifest.permission.SEND_SMS},2);
                    }
                    else{
                        Log.i("debug","Autorisé");
                        send();
                    }
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

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                position();
            }
            else {
                Toast.makeText(EmployeCompanies.this, "Vous devez autoriser l'accès à la localisation", Toast.LENGTH_LONG).show();
            }
        }

        else if(requestCode==2){
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    send();
                }
                else{
                    Toast.makeText(EmployeCompanies.this, "Vous devez autoriser l'envoi des sms", Toast.LENGTH_SHORT).show();
                    message.message(EmployeCompanies.this,"Echec","Votre message n'a pas été envoyé.",0);
                }
        }
        else{
                Log.i("debug","Probleme permission");
        }

    }

    private void send() {
        SmsManager smsManager = SmsManager.getDefault();

        SimpleDateFormat format = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(format.format(new Date()));

        String msg = "L'employé "+ preferences.getString("user",null)+" vous avertit de son ";
        msg += (hour < 13) ? "arrivée." : "départ.";

        smsManager.sendTextMessage(employeeNumber, null, msg, null, null);
        message.message(EmployeCompanies.this,"Confirmation","Votre message a été envoyé.",0);
        sendMessage.setEnabled(false);
        sendMessage.setText("Message envoyé");
    }

    private void position() {
        Log.i("debug","Actualiser votre position");
        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
            work_place.update_location();
            sendMessage.setEnabled(false);
            sendMessage.setText("Patientez...");
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {
                    if (work_place.insideZone()) {
                        sendMessage.setText("Envoyer");
                        textView1.setVisibility(View.INVISIBLE);
                    }
                    else
                        sendMessage.setText("Vérifier la position");
                    sendMessage.setEnabled(true);
                }
            }, 3000);   //3 seconds
        }
        else {
            message.message(EmployeCompanies.this,"Connexion Internet","Activer votre connexion internet.",0);
        }
    }
}
