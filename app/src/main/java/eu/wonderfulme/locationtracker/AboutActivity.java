package eu.wonderfulme.locationtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_aboutActivity) Toolbar mToolbar;
    @BindView(R.id.tv_about_openCSV_website) TextView mOpenCsvWebsiteTextView;
    @BindView(R.id.tv_about_openCSV_license) TextView mOpenCsvLicenseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mOpenCsvWebsiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mOpenCsvLicenseTextView.setMovementMethod(LinkMovementMethod.getInstance());


    }
}
