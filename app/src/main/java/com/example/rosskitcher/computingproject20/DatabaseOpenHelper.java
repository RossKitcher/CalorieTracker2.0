package com.example.rosskitcher.computingproject20;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseOpenHelper extends SQLiteAssetHelper{ // extends SQLiteAssetHelper instead of SQLiteOpenHelper

    private static final String DATABASE_NAME = "newTest3.db"; // constant to store database name

    private static final int DATABASE_VERSION = 1; // database version

    public DatabaseOpenHelper(Context context) { // constructor

        // extracts database from assets folder and copies into the application's private data directory
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
