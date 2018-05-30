package eu.wonderfulme.locationtracker;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Check if the user is online.

        MainFragment mainFragment = MainFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frameLayout_main_fragment, mainFragment).commit();

    }
}
