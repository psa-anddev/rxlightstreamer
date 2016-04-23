package com.psa.rxlightstreamer.sample.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

/**
 * <p>This is the service that will handle LightStreamer.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class LightStreamerService extends Service {
    @Inject
    RxLightStreamerClient mLightStreamerClient;
    @Inject
    ServiceMediator mServiceMediator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((SampleApplication) getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceMediator.getServiceSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(serviceEvent -> {
                   switch (serviceEvent.getEventType())
                   {
                       case CONNECT:
                           mLightStreamerClient.connect(serviceEvent.getLSHost(), serviceEvent.getAdapterSet());
                           mLightStreamerClient.getClientStatusObservable()
                                   .subscribe(mServiceMediator.getClientStatusSubject());
                           break;
                       case DISCONNECT:
                           mLightStreamerClient.disconnect();
                           break;
                       case SUBSCRIBE:
                           mLightStreamerClient.subscribe(serviceEvent.getSubscription());
                           break;
                       case UNSUBSCRIBE:
                           mLightStreamerClient.unsubscribe(serviceEvent.getSubscription());
                           break;
                   }
                });

        return START_STICKY;
    }
}
