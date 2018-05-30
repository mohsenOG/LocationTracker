package eu.wonderfulme.locationtracker;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    public static final String EXTRA_RECORD_PERIOD = "EXTRA_RECORD_PERIOD";
    private static final String NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 100;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private long mRecordPeriodInSeconds;


    public LocationService() { }


    @Override
    public void onCreate() {
        super.onCreate();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    // TODO Write location on db
                }
            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRecordPeriodInSeconds = intent.getIntExtra(EXTRA_RECORD_PERIOD, 10);
        mLocationRequest.setInterval(mRecordPeriodInSeconds * 1000);
        //TODO permission request.
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);


        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is title")
                .setContentText("This is content text")
                .setSmallIcon(R.drawable.launcher_base)
                .setTicker("This is the ticker")
                .build();

        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
    }
}
