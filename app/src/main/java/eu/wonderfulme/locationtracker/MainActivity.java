package eu.wonderfulme.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO Add about page with an action. look at liscenseing

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                                                                    SwipeRefreshLayout.OnRefreshListener, MainFragment.RecordingButtonListener{

    private boolean mIsRecording = false;
    private GoogleApiClient mGoogleApiClient;
    @BindView(R.id.swipeLayout_main_activity) protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.linearLayout_mainActivity) protected LinearLayout mLinearLayout;
    @BindView(R.id.toolbar_mainActivity) protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));


        // Turn the GPS on
        boolean isGpsOn = Utils.isLocationEnabled(this);
        if (!isGpsOn) {
            ErrorFragment errorFragment = ErrorFragment.newInstance(getString(R.string.error_gps_disabled));
        }

        // Connect to api client.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        //TODO On saveInstanceState

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        FragmentManager fm = getSupportFragmentManager();
        if (isApiConnected) {
            MainFragment mainFragment = MainFragment.newInstance();
            fm.beginTransaction().add(R.id.frameLayout_main_fragment, mainFragment).commit();
        } else {
            ErrorFragment errorFragment = ErrorFragment.newInstance(getString(R.string.error_google_api_is_not_available));
            fm.beginTransaction().add(R.id.frameLayout_main_fragment, errorFragment).commit();
        }
    }

    @Override
    public void onRefresh() {
        if(mIsRecording) {
            Snackbar.make(mLinearLayout, getResources().getString(R.string.snackbar_refresh_disabled), Snackbar.LENGTH_SHORT).show();
        } else {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onRecordingButtonClicked(boolean isRecording) {
        mIsRecording = isRecording;
    }

    private void showAbout() {
        if (mIsRecording) {
            // Show error to stop at first
            Snackbar.make(mLinearLayout, R.string.snackbar_goto_about_error, Snackbar.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }
}
