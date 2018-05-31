package eu.wonderfulme.locationtracker.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class RoomDbSingleton {
    private static LocationDatabase instance = null;

    public static LocationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, LocationDatabase.class, "locationDb").build();
        }
        return instance;
    }

    private RoomDbSingleton() { }
}
