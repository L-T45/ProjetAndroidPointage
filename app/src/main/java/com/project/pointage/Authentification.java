package com.project.pointage;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import java.security.MessageDigest;


public class Authentification {


    private Context auth_context  ;

    public Authentification(Context context){
        this.auth_context = context;
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

    public boolean checkUser(String username,String password,Database database){
        boolean new_password;
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
            new_password = true;
        }else{
            new_password = false;
        }

        Log.i("debug","isCheckUser: "+new_password+" "+cursor.getCount());
        return new_password;
    }

}
