package eu.wonderfulme.locationtracker.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class LocationTrackerContract {
    private LocationTrackerContract() {}

    static final String AUTHORITY = "eu.wonderfulme.locationtracker";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_LOCATIONS = "locations";

    public static class LocationTrackerEntry implements BaseColumns {
        public static final Uri CONTENT_URI_LOCATIONS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATIONS).build();

        public static final String TABLE_NAME = "location_tracker";
        public static final String TIMESTAMP = "timestamp";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ALTITUDE = "altitude";
        public static final String SPEED = "speed";

    }

}
