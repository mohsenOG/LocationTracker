package eu.wonderfulme.locationtracker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorFragment extends Fragment {

    public final static String ERROR_MSG_FRAGMENT = "ERROR_MSG_FRAGMENT";

    private String mErrorMsg;
    @BindView(R.id.textView_error) TextView mErrorTextView;

    public ErrorFragment() { }

    public static ErrorFragment newInstance(String errorMsg) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putString(ERROR_MSG_FRAGMENT, errorMsg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mErrorMsg = getArguments().getString(ERROR_MSG_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        ButterKnife.bind(this, view);
        mErrorTextView.setText(mErrorMsg);
        return view;
    }

}
