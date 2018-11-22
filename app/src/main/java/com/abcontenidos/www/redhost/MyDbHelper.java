package com.abcontenidos.www.redhost;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "redoff.db";
    public String table;

    public MyDbHelper(Context context, String table) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.table = table;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE categories " + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "details TEXT NOT NULL,"
                + "selected TEXT NOT NULL DEFAULT 'false',"
                + "image TEXT NOT NULL,"
                + "UNIQUE (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
