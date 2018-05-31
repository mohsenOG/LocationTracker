package eu.wonderfulme.locationtracker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseWrapper {
    private SQLiteDatabase mDb;
    private LocationTrackerDbHelper dbHelper = null;

    DatabaseWrapper(boolean isWriteable) {
        if (isWriteable) {
            mDb = dbHelper.getWritableDatabase();
        } else {
            mDb = dbHelper.getReadableDatabase();
        }
    }

    Cursor query(String table, String[] columns, String selection,
                 String[] selectionArgs,
                 String orderBy) {
        return mDb.query(table, columns, selection, selectionArgs, null, null, orderBy);
    }

    long insert(String table, ContentValues values) {
        return mDb.insert(table, null, values);
    }

    int delete(String table) {
        return mDb.delete(table, null, null);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mDb.close();
        dbHelper.close();
    }}
