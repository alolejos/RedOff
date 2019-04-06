package com.abcontenidos.www.redhost.Dbases;

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
                + "id_database TEXT NOT NULL,"
                + "name TEXT NOT NULL,"
                + "details TEXT NOT NULL,"
                + "selected TEXT NOT NULL DEFAULT 'false',"
                + "image TEXT NOT NULL,"
                + "UNIQUE (id))");

        db.execSQL("CREATE TABLE user " + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_base TEXT NOT NULL,"
                + "name TEXT NOT NULL,"
                + "mail TEXT NOT NULL,"
                + "token TEXT NOT NULL,"
                + "address TEXT NOT NULL,"
                + "gender TEXT NOT NULL,"
                + "birthday TEXT NOT NULL,"
                + "image BLOB NOT NULL,"
                + "commerce_id TEXT,"
                + "commerce_name TEXT,"
                + "commerce_address TEXT,"
                + "commerce_phone TEXT,"
                + "commerce_email TEXT,"
                + "commerce_web TEXT,"
                + "commerce_image TEXT,"
                + "UNIQUE (id))");
        db.execSQL("CREATE TABLE posts " + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "details TEXT NOT NULL,"
                + "category TEXT NOT NULL,"
                + "image TEXT NOT NULL,"
                + "commerce TEXT NOT NULL,"
                + "address TEXT NOT NULL,"
                + "phone TEXT NOT NULL,"
                + "UNIQUE (id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
