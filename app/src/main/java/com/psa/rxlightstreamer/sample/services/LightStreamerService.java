package com.psa.rxlightstreamer.sample.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.lightstreamer.ls_client.PushConnException;
import com.lightstreamer.ls_client.PushServerException;
import com.lightstreamer.ls_client.PushUserException;
import com.lightstreamer.ls_client.SubscrException;
import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedLSClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

/**
 * <p>This is the service that will handle LightStreamer.</p>
 *
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class LightStreamerService extends Service
{
    @Inject
    RxLightStreamerClient mLightStreamerClient;
    @Inject
    RxNonUnifiedLSClient mRxNonUnifiedLSClient;
    @Inject
    ServiceMediator mServiceMediator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        ((SampleApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mServiceMediator.getServiceSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(serviceEvent -> {
                    boolean unifiedEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("unified", false);
                    switch (serviceEvent.getEventType())
                    {
                        case CONNECT:
                            if (unifiedEnabled)
                            {
                                mLightStreamerClient.connect(serviceEvent.getLSHost(), serviceEvent.getAdapterSet());
                                mLightStreamerClient.getClientStatusObservable()
                                        .subscribe(mServiceMediator.getClientStatusSubject());
                            }
                            else
                            {
                                mRxNonUnifiedLSClient.connect(serviceEvent.getLSHost(), serviceEvent.getAdapterSet());
                                mRxNonUnifiedLSClient.getClientStatusObservable()
                                        .subscribe(mServiceMediator.getClientStatusSubject());
                            }
                            break;
                        case DISCONNECT:
                            if (unifiedEnabled)
                                mLightStreamerClient.disconnect();
                            else
                                mRxNonUnifiedLSClient.disconnect();
                            break;
                        case SUBSCRIBE:
                            if (unifiedEnabled)
                            {
                                RxSubscription subscription = serviceEvent.getSubscription();
                                if (subscription != null)
                                    mLightStreamerClient.subscribe(subscription);
                            }
                            else
                            {
                                RxNonUnifiedSubscription subscription = serviceEvent.getNonUnifiedSubscription();
                                if (subscription != null)
                                    try
                                    {
                                        mRxNonUnifiedLSClient.subscribe(subscription);
                                    }
                                    catch (SubscrException | PushServerException | PushUserException | PushConnException e)
                                    {
                                        e.printStackTrace();
                                    }
                            }
                            break;
                        case UNSUBSCRIBE:
                            if (unifiedEnabled)
                            {
                                RxSubscription subscription = serviceEvent.getSubscription();
                                if (subscription != null)
                                    mLightStreamerClient.unsubscribe(subscription);
                            }
                            else
                            {
                                RxNonUnifiedSubscription subscription = serviceEvent.getNonUnifiedSubscription();
                                if (subscription != null)
                                    try
                                    {
                                        mRxNonUnifiedLSClient.unsubscribe(subscription);
                                    }
                                    catch (PushConnException | PushServerException | SubscrException e)
                                    {
                                        e.printStackTrace();
                                    }
                            }
                            break;
                    }
                });

        return START_STICKY;
    }
}
