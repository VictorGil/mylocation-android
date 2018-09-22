package net.devaction.mylocation.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.devaction.mylocation.db.Schema.LocationServiceStateTable;
import static net.devaction.mylocation.db.Schema.LocationServiceStateTable.Cols.STATE;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class MyLocationDbHelper extends SQLiteOpenHelper{
    //this is important, is the version of the DB schema
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mylocation.db";

    private static MyLocationDbHelper INSTANCE = null;

    public static MyLocationDbHelper getInstance(Context appContext){
        if (INSTANCE == null)
            INSTANCE = new MyLocationDbHelper(appContext);
        return INSTANCE;
    }

    private MyLocationDbHelper(Context appContext) {
        super(appContext, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LocationServiceStateTable.NAME + "(" + //
                STATE + " text not null unique " + ")" );

        //Initially the location service is off
        ContentValues values = new ContentValues();
        values.put(Schema.LocationServiceStateTable.Cols.STATE, Schema.OFF);
        db.insert(Schema.LocationServiceStateTable.NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

