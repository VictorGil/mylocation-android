package net.devaction.mylocation.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static net.devaction.mylocation.db.Schema.LocationServiceStateTable.Cols.STATE;
import static net.devaction.mylocation.db.Schema.LocationServiceStateTable;
import static net.devaction.mylocation.db.Schema.ON;
import static net.devaction.mylocation.db.Schema.OFF;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class LocationServiceController{
    private static final String[] STATE_COLUMN = {STATE};

    public static boolean isServiceEnabled(Context context){
        final SQLiteDatabase db = MyLocationDbHelper.getInstance(context).getReadableDatabase();


        final Cursor cursor = db.query(LocationServiceStateTable.NAME,
                STATE_COLUMN, null, null, null, null, null);

        cursor.moveToFirst();
        final String stateValue = cursor.getString(cursor.getColumnIndex(STATE));

        if (stateValue == null){
            Log.e(LocationServiceController.class.getSimpleName(), "Location service state is null in the database");
            return false;
        }

        if (stateValue.equals(ON)){
            Log.d(LocationServiceController.class.getSimpleName(), "Location service state is " + ON);
            return true;
        }

        //state is "off"
        Log.d(LocationServiceController.class.getSimpleName(), "Location service state is " + OFF);
        return false;
    }

    public static OnOrOff switchOnOrOff(Context context){
        if (isServiceEnabled(context)){
            disable(context);
            return OnOrOff.OFF;
        }

        enable(context);
        return OnOrOff.ON;
    }

    static void enable(Context context){
        final SQLiteDatabase db = MyLocationDbHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATE, ON);

        String whereClause = STATE + " = ?";
        String[] whereArgs = new String[]{OFF};

        //update locationServiceState set state = 'ON' where state = 'OFF';
        db.update(LocationServiceStateTable.NAME, values, whereClause,  whereArgs);
    }

    static void disable(Context context){
        final SQLiteDatabase db = MyLocationDbHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATE, OFF);

        String whereClause = STATE + " = ?";
        String[] whereArgs = new String[]{ON};

        //update locationServiceState set state = 'off' where state = 'on';
        db.update(LocationServiceStateTable.NAME, values, whereClause,  whereArgs);
    }
}

