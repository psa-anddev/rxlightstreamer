package com.psa.rxlightstreamer.sample.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>This fragment represents a status fragment.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StatusFragment extends Fragment {
    @Bind(R.id.status_text)
    TextView mStatusText;
    @Inject
    ServiceMediator mServiceMediator;

    private Subscription mRxClientSubscription;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SampleApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
        mRxClientSubscription = mServiceMediator.getClientStatusSubject().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        c -> mStatusText.setText(c.getLightStreamerStatus()),
                        e -> mStatusText.setText("Error: " + e.getMessage()));
    }

    @Override
    public void onStop() {
        super.onStop();
        mRxClientSubscription.unsubscribe();
    }
}
