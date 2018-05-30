package eu.wonderfulme.locationtracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {


    private boolean mIsRecording = false;
    private int mPeriodInSeconds;
    @BindView(R.id.editText_record_period) protected EditText mRecordPeriodEditText;
    @BindView(R.id.fab_start_stop_record) protected FloatingActionButton mRecordFab;

    public MainFragment() { }

    public static MainFragment newInstance() { return new MainFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mRecordFab.setOnClickListener(new RecordFabClickListener());
        mPeriodInSeconds = Integer.parseInt(mRecordPeriodEditText.getText().toString());
        return view;
    }


    private class RecordFabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Handle location permission here.
            if (!mIsRecording) {
                //Handle in case of start.
                startStopLocationService(true);

            } else {
                //Handle in case of stop.
                startStopLocationService(false);
            }
        }
    }

    private void startStopLocationService(boolean start) {
        Intent intent = new Intent(getActivity(), LocationService.class);
        if (start) {
            mIsRecording = true;
            mRecordFab.setImageResource(android.R.drawable.ic_media_pause);
            Objects.requireNonNull(getActivity()).startService(intent);
        } else {
            mIsRecording = false;
            mRecordFab.setImageResource(android.R.drawable.ic_media_play);
            Objects.requireNonNull(getActivity()).stopService(intent);
        }
    }



}
