package com.psa.rxlightstreamer.sample.helpers;

import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.ClientStatus;

import rx.subjects.BehaviorSubject;

/**
 * <p>This class connects the service with the actual classes that receive the events.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class ServiceMediator {
    //region Field declarations
    private BehaviorSubject<ServiceEvent> mServiceSubject;
    private BehaviorSubject<ClientStatus> mClientStatusSubject;
    //endregion

    //region Constructor
    /**
     * <p>Instantiates a mediator.</p>
     */
    public ServiceMediator() {
        mServiceSubject = BehaviorSubject.create();
        mClientStatusSubject = BehaviorSubject.create(ClientStatus.DISCONNECTED);
    }
    //endregion

    //region getters
    /**
     * <p>Returns the subject that will send events to the service.</p>
     * @return a behaviour subject that controls the service.
     */
    public BehaviorSubject<ServiceEvent> getServiceSubject() {
        return mServiceSubject;
    }

    /**
     * <p>A behaviour that will re-emit the client events.</p>
     * @return client status subject.
     */
    public BehaviorSubject<ClientStatus> getClientStatusSubject() {
        return mClientStatusSubject;
    }
    //endregion

    //region service controllers

    /**
     * <p>Connects the LS client in the server.</p>
     * @param host is the host.
     * @param adapterSet is the adapter set.
     */
    public void connect(String host, String adapterSet)
    {
        mServiceSubject.onNext(
                new ServiceEvent.Builder(ServiceEvent.EventType.CONNECT)
                .setLSHost(host).setAdapterSet(adapterSet)
                .build()
        );
    }

    /**
     * <p>Disconnect the current LS session.</p>
     */
    public void disconnect()
    {
        mServiceSubject.onNext(new ServiceEvent.Builder(ServiceEvent.EventType.DISCONNECT).build());
    }

    /**
     * <p>Subscribes the given subscription.</p>
     * @param subscription is the subscription.
     */
    public void subscribe(RxSubscription subscription)
    {
        mServiceSubject.onNext(
                new ServiceEvent.Builder(ServiceEvent.EventType.SUBSCRIBE)
                .setUnifiedSubscription(subscription)
                .build()
        );
    }

    /**
     * <p>Unsubscribe the given subscription.</p>
     * @param subscription subscription to unsubscribe.
     */
    public void unsubscribe(RxSubscription subscription)
    {
        mServiceSubject.onNext(
                new ServiceEvent.Builder(ServiceEvent.EventType.UNSUBSCRIBE)
                        .setUnifiedSubscription(subscription)
                        .build()
        );
    }

    /**
     * <p>Subscribes the given subscription.</p>
     * @param subscription is the subscription.
     */
    public void subscribe(RxNonUnifiedSubscription subscription)
    {
        mServiceSubject.onNext(
                new ServiceEvent.Builder(ServiceEvent.EventType.SUBSCRIBE)
                        .setNonUnifiedSubscription(subscription)
                        .build()
        );
    }

    /**
     * <p>Unsubscribe the given subscription.</p>
     * @param subscription subscription to unsubscribe.
     */
    public void unsubscribe(RxNonUnifiedSubscription subscription)
    {
        mServiceSubject.onNext(
                new ServiceEvent.Builder(ServiceEvent.EventType.UNSUBSCRIBE)
                        .setNonUnifiedSubscription(subscription)
                        .build()
        );
    }
    //endregion
}
