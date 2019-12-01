package com.project.pointage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Message {


    public void message(Context context , String title, String message, int number){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.action_ok,null);
        if(number == 1){
            builder.setNegativeButton(R.string.action_no, null);
        }
        builder.setMessage(message);
        if(title != null){
            builder.setTitle(title);
        }
        AlertDialog alert = builder.create();
        alert.show();
    }
}
