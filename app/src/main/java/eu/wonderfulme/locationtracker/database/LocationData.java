package eu.wonderfulme.locationtracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import eu.wonderfulme.locationtracker.R;

@Entity
public class LocationData {

    public static String[] getDbHeaders(Context context) {
        return new String[] {context.getResources().getString(R.string.table_header_timestamp),
                context.getResources().getString(R.string.table_header_latitude), context.getResources().getString(R.string.table_header_longitude),
                context.getResources().getString(R.string.table_header_altitude), context.getResources().getString(R.string.table_header_speed)};
    }

    public static String[] locationCsvRowBuilder(final String timestamp, final double latitude, final double longitude, final double altitude, final float speed) {
        return new String[] {timestamp, String.valueOf(latitude), String.valueOf(longitude),
                        String.valueOf(altitude), String.valueOf(speed)};

    }

    public LocationData(@NonNull String timestamp, double latitude, double longitude, double altitude, float speed) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.speed = speed;
    }

    @PrimaryKey
    @NonNull
    private String timestamp;
    private double latitude;
    private double longitude;
    private double altitude;
    private float speed;


    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
