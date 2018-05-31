package eu.wonderfulme.locationtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import eu.wonderfulme.locationtracker.database.LocationTrackerContract.*;


public class LocationTrackerDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locationTracker.db";
    private static final int DATABASE_VERSION = 1;

    public LocationTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TRACKER_TABLE = "CREATE TABLE " +
                LocationTrackerEntry.TABLE_NAME + " (" +
                LocationTrackerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationTrackerEntry.TIMESTAMP + " TEXT," +
                LocationTrackerEntry.LATITUDE + " REAL," +
                LocationTrackerEntry.LONGITUDE + " REAL," +
                LocationTrackerEntry.ALTITUDE + " REAL," +
                LocationTrackerEntry.SPEED + " REAL" +
                ");";

        db.execSQL(SQL_CREATE_LOCATION_TRACKER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // In this very small app it works perfectly but it is better to use ALTER command
        // instead of DROP.
        // https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
        db.execSQL("DROP TABLE IF EXISTS " + LocationTrackerEntry.TABLE_NAME);
        onCreate(db);
    }
}
