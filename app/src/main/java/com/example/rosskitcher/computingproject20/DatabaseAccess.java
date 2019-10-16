package com.example.rosskitcher.computingproject20;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


// Class to establish a connection to the database
public class DatabaseAccess {

    // variable declarations
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    //private constructor to avoid object creation from outside classes
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);


    }

    // return a single instance of DatabaseAccess
    public static DatabaseAccess getInstance(Context context) {
        if (instance==null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    //open database connection
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    //close database connection
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    // query to get all item names
    public List<String> getItems() {
        List<String> names = new ArrayList<>();

        // query
        Cursor cursor = database.rawQuery("SELECT Long_Desc FROM FD_GROUP", null);


        // cursor provides access to result set returned by a query
        cursor.moveToFirst(); // moves cursor to first row
        while (!cursor.isAfterLast()) { // this loop iterates through all of the data
            for(int i=0; i < cursor.getColumnCount(); i++) // for loop to ensure all columns are checked
                names.add(cursor.getString(i)); // add item
            cursor.moveToNext(); // next row
        }
        cursor.close();
        return names; // return list
    }

    // query to get all nutrients
    public List<String> getNutrients(String name) {
        List<String> items = new ArrayList<>();

        name = "'" + name + "'"; // to avoid sqlexception error

        // query
        Cursor cursor = database.rawQuery("Select ABBREV.Lipid_Tot, ABBREV.Protein, " +
                "ABBREV.Carbohydrt, ABBREV.Sugar_Tot, ABBREV.Fiber_TD, " +
                "ABBREV.Vit_A_IU, ABBREV.Vit_C, ABBREV.Vit_D_IU, ABBREV.Vit_B6, ABBREV.Vit_B12, " +
                "ABBREV.Iron, ABBREV.Magnesium, ABBREV.Potassium, ABBREV.Calcium, ABBREV.Sodium " +
                "FROM FD_GROUP " +
                "INNER JOIN ABBREV " +
                "ON FD_GROUP.NDB_No = ABBREV.NDB_No " +
                "WHERE FD_GROUP.Long_Desc = " + name, null);


        // checks every field of every column
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            for (int i = 0; i < cursor.getColumnCount(); i++)
                items.add(cursor.getString(i));
            cursor.moveToNext();
        }
        cursor.close();

        return items; // return list
    }

    // query to get calories
    public List<String> getItemCal(String name) {
        List<String> cal = new ArrayList<>();

        name = "'" + name + "'";

        // query
        Cursor cursor = database.rawQuery("Select ABBREV.Energ_Kcal " +
                "FROM FD_GROUP " +
                "INNER JOIN ABBREV " +
                "ON FD_GROUP.NDB_No = ABBREV.NDB_No " +
                "WHERE FD_GROUP.Long_Desc = " + name, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            for(int i=0; i < cursor.getColumnCount(); i++)
                cal.add(cursor.getString(i));
            cursor.moveToNext();
        }
        cursor.close();
        return cal;
    }


    // query to get calories
    public List<String> getUPCCodes(String code) {
        List<String> name = new ArrayList<>();

         code = "'" + code + "'";

        Cursor cursor = database.rawQuery("Select FD_GROUP.Long_Desc " +
                "FROM FD_GROUP " +
                "INNER JOIN UPC " +
                "ON FD_GROUP.NDB_No = UPC.NDB_No " +
                "WHERE UPC.Code = " + code, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            for(int i=0; i < cursor.getColumnCount(); i++)
                name.add(cursor.getString(i));
            cursor.moveToNext();
        }
        cursor.close();
        return name;
    }

}
