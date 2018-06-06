package eu.wonderfulme.locationtracker;

import android.content.Intent;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                                                                    SwipeRefreshLayout.OnRefreshListener, MainFragment.RecordingButtonListener{

    private static final String SAVE_STATE_IS_RECORDING = "SAVE_STATE_IS_RECORDING";

    private boolean mIsRecording = false;
    private GoogleApiClient mGoogleApiClient;
    @BindView(R.id.swipeLayout_main_activity) protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.linearLayout_mainActivity) protected LinearLayout mLinearLayout;
    @BindView(R.id.toolbar_mainActivity) protected Toolbar mToolbar;
    private InterstitialAd mInterstitialAd;
    private AdRequest mAdRequest;
    private MainFragment mMainFragment;
    private ErrorFragment mErrorFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //TODO W/Ads: Loading already in progress, saving this object for future refreshes.
        // init Admob
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        //init interstitialAd
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_unit_id_about));
        mInterstitialAd.setAdListener(new AboutActivityAdListener());
        mAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //SaveInstance
        if (savedInstanceState != null) {
            mIsRecording = savedInstanceState.getBoolean(SAVE_STATE_IS_RECORDING);
            mErrorFragment = (ErrorFragment) getSupportFragmentManager().getFragment(savedInstanceState, "error fragment");
            mMainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "main fragment");
        }
        mFragmentManager = getSupportFragmentManager();
        // Turn the GPS on
        boolean isGpsOn = Utils.isLocationEnabled(this);
        if (!isGpsOn) {
            mErrorFragment = ErrorFragment.newInstance(getString(R.string.error_gps_disabled));
            mFragmentManager.beginTransaction().replace(R.id.frameLayout_main_fragment, mErrorFragment).commit();
        }

        // Connect to api client.
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_STATE_IS_RECORDING, mIsRecording);
        if (mMainFragment != null) {
            getSupportFragmentManager().putFragment(outState, "main fragment", mMainFragment);
        }
        if (mErrorFragment != null) {
            getSupportFragmentManager().putFragment(outState, "error fragment", mErrorFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInterstitialAd.loadAd(mAdRequest);
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
            case R.id.menu_item_about: {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    showAbout();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkApiConnectionAndShowFragments(true);

    }

    @Override
    public void onConnectionSuspended(int i) {
        checkApiConnectionAndShowFragments(false);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        checkApiConnectionAndShowFragments(false);
    }

    private void checkApiConnectionAndShowFragments(boolean isApiConnected) {
        if (isApiConnected && mMainFragment == null && mErrorFragment == null) {
            mMainFragment = MainFragment.newInstance();
            mFragmentManager.beginTransaction().replace(R.id.frameLayout_main_fragment, mMainFragment).commit();
        } else if (!isApiConnected && mErrorFragment == null) {
            mErrorFragment = ErrorFragment.newInstance(getString(R.string.error_google_api_is_not_available));
            mFragmentManager.beginTransaction().replace(R.id.frameLayout_main_fragment, mErrorFragment).commit();
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

    private class AboutActivityAdListener extends AdListener {
        @Override
        public void onAdClosed() {
            showAbout();
        }

    }
}
