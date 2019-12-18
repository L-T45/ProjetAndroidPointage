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
    private boolean status_new_password = false;

    public Authentification(Context context,String user,String password){
        this.auth_context = context;
        this.new_password = password;
        this.username =user;
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

    private void Session(String username,int type){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.auth_context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("user",username);
        edit.putInt("fonction",type);
        edit.apply();
        edit.commit();
        Log.i("debug","Create the session: "+preferences.getString("user",null));
    }

    public boolean checkUser(Database database){
        boolean new_password;
        String username = getUsername();
        String password = getPassword();

        SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
        String request = "SELECT name, type FROM employe WHERE username='"+username+"' AND password='"+get_hash_pasword(password)+"';";
        Cursor cursor  = sqLiteDatabase.rawQuery(request,null);
        if(cursor.getCount()== 1){
            if (cursor.moveToFirst()){
                do {
                    // Passing values
                        Session(cursor.getString(0),cursor.getInt(1));
                    // Do something Here with values
                } while(cursor.moveToNext());
            }
            status_new_password = !status_new_password;
        }

        Log.i("debug","isCheckUser:  "+cursor.getCount()+" "+status_new_password);
        return status_new_password;
    }


    public void createUser(DataSnapshot dataSnapshot,Database database){
        Log.i("debug","Create the user inside the database Firebase");
        String name = dataSnapshot.child("nom").getValue(String.class);
        String firstname = dataSnapshot.child("prenom").getValue(String.class);
        int type = dataSnapshot.child("fonction").getValue(int.class);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",this.getUsername());
        contentValues.put("password",this.get_hash_pasword(getPassword()));
        contentValues.put("name",name);
        contentValues.put("firstname",firstname);
        contentValues.put("type",type);
        sqLiteDatabase.insert("employe",null,contentValues);
        Session(name,type);
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

