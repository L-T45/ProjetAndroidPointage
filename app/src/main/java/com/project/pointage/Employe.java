package com.project.pointage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Employe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employe);

        Button button = (Button)findViewById(R.id.button1);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                checkPermission();
             }
         }
         );
    }
    private void envoiesms(){
        SmsManager.getDefault().sendTextMessage("0672038043", null,
                "Un employé vous avertis de son arrivé ou de son départ." +
                        "Vous pouvez le consulter dans la liste de départ ou d'arrivé des employés sur l'application.",
                null, null);
    }
    private void checkPermission() {
        if(ActivityCompat.checkSelfPermission(Employe.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Employe.this, new String[]{Manifest.permission.SEND_SMS}, 2);
        }
        else{
            envoiesms();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //on appelle la fonction d'envoie
                envoiesms();
            }
            else
            {
                Toast.makeText(Employe.this,"Vous devez accepter l'autorisation des messages",Toast.LENGTH_LONG).show();
            }
        }
    }
}
