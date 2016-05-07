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
import com.psa.rxlightstreamer.sample.presenters.StatusBarPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>This fragment represents a status fragment.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StatusFragment extends Fragment
{
    @Bind(R.id.status_text)
    TextView mStatusText;
    @Inject
    StatusBarPresenter mStatusBarPresenter;

    private Subscription mPresenterSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((SampleApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
        mStatusBarPresenter.setView(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mPresenterSubscription = mStatusBarPresenter.getItemsObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        integer -> mStatusText.setText(integer)
                );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mPresenterSubscription.unsubscribe();
        mStatusBarPresenter.setView(null);
    }
}
