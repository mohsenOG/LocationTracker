package eu.wonderfulme.locationtracker.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {LocationData.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}
