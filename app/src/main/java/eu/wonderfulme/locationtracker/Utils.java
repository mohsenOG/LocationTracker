package eu.wonderfulme.locationtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {}

    static String getFormattedTime (long timeInMilliseconds) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date resultDate = new Date(timeInMilliseconds);
        return simpleDateFormat.format(resultDate);
    }

    static String getFormattedFileName() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date resultDate = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(resultDate) + "_output.csv";
    }

    /**
     * @return true if online otherwise false.
     * @see https://stackoverflow.com/a/4009133/6072457
     */
    @SuppressWarnings("JavadocReference")
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
