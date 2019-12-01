package com.project.pointage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE = "employe.db";
    private static final String TABLE    = "employe";
    private static final int VERSION     = 1;


    public Database(Context context){
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String request ="CREATE TABLE "+this.TABLE+" (id INTEGER PRIMARY KEY, username TEXT, password TEXT, name TEXT,firstname TEXT, type INTEGER)";
        sqLiteDatabase.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String request = "DROP TABLE IF EXISTS "+this.DATABASE;
        sqLiteDatabase.execSQL(request);
        onCreate(sqLiteDatabase);
    }
}
