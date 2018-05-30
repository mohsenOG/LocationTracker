package eu.wonderfulme.locationtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        //TODO Check if the user is online.
        //TODO On saveInstanceState
        //TODO Check if the googlePlay API is installed (Google play permission)
        // https://developers.google.com/android/guides/setup

        MainFragment mainFragment = MainFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frameLayout_main_fragment, mainFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setIsApiConnected(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        setIsApiConnected(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setIsApiConnected(false);
    }

    private void setIsApiConnected(boolean isApiConnected) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.pref_is_api_connected), isApiConnected);
        editor.apply();
    }

}
