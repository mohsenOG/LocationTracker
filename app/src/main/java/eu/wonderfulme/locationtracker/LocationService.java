package eu.wonderfulme.locationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import eu.wonderfulme.locationtracker.database.LocationData;
import eu.wonderfulme.locationtracker.database.RoomDbSingleton;

public class LocationService extends Service implements LocationListener {

    public static final String EXTRA_RECORD_PERIOD = "EXTRA_RECORD_PERIOD";
    private static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";
    private static final String NOTIFICATION_CHANNEL_ID = "100";
    private static final int NOTIFICATION_ID = 110;
    private LocationRequest mLocationRequest;
    private MyLocationCallback mLocationCallback;
    private long mRecordPeriodInSeconds;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new MyLocationCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRecordPeriodInSeconds = intent.getIntExtra(EXTRA_RECORD_PERIOD, 10);
        mLocationRequest.setInterval(mRecordPeriodInSeconds * 1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_locationservice_title))
                .setContentText(getString(R.string.notification_locationservice_content))
                .setSmallIcon(R.drawable.notification)
                .setColor(getResources().getColor(R.color.colorAccent));
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, builder.build());

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

    @Override
    public void onLocationChanged(Location location) {
        mLocationCallback.saveLocationOnDatabase(location);
    }

    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) return;
            Location location = locationResult.getLastLocation();
            saveLocationOnDatabase(location);
        }

        void saveLocationOnDatabase(Location location) {
            String timestamp = Utils.getFormattedTime(System.currentTimeMillis());
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = -1.;
            if (location.hasAltitude()) {
                location.getAltitude();
            }
            float speed = -1.f;
            if (location.hasSpeed()) {
                speed = location.getSpeed();
            }
            LocationData dbData = new LocationData(timestamp, latitude, longitude, altitude, speed);
            new DatabaseAsync().execute(dbData);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DatabaseAsync extends AsyncTask<LocationData, Void, Void> {
        @Override
        protected Void doInBackground(LocationData... locationData) {
            RoomDbSingleton.getInstance(getApplicationContext()).locationDao().insertSingleRecord(locationData[0]);
            return null;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
