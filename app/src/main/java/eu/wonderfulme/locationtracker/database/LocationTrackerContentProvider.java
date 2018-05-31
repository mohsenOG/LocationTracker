package eu.wonderfulme.locationtracker.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

public class LocationTrackerContentProvider extends ContentProvider {

    private DatabaseWrapper mDb;

    @Override
    public boolean onCreate() {
        mDb = new DatabaseWrapper(true);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor ret = mDb.query(LocationTrackerContract.LocationTrackerEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) { return null; }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = mDb.insert(LocationTrackerContract.LocationTrackerEntry.TABLE_NAME, values);
        Uri ret;
        if (id > 0)
            ret = ContentUris.withAppendedId(LocationTrackerContract.LocationTrackerEntry.CONTENT_URI_LOCATIONS, id);
        else
            throw new SQLException("Failed to insert row into " + uri);
        return ret;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        mDb.delete(LocationTrackerContract.LocationTrackerEntry.TABLE_NAME);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
