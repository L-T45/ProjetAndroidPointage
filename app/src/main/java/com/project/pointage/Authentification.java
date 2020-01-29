package com.project.pointage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;

import java.security.MessageDigest;


public class Authentification {

    private Context auth_context;
    private String new_password;
    private String username;

    public Authentification(Context context,String user,String password){
        this.auth_context = context;
        this.new_password = password;
        this.username = user;
    }

    public String get_hash_pasword(String password){

        StringBuffer newPassordHash = new StringBuffer();
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-256");
            messagedigest.update(password.getBytes());
            byte[] hash = messagedigest.digest();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) newPassordHash.append('0');
                newPassordHash.append(hex);
            }

        } catch (Exception exception) {
            Log.i("debug", "getPassword: "+ exception.getMessage());
        }
        Log.i("debug"," the new password: "+newPassordHash.toString());
        return  newPassordHash.toString();
    }

    public void session(String username,int type){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.auth_context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("user",username);
        edit.putInt("fonction",type);
        edit.apply();
        edit.commit();
        Log.i("debug","Create the session: "+preferences.getString("user",null));
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.new_password;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

