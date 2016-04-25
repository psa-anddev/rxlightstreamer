package com.psa.rxlightstreamer.core;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.ConnectionOptions;
import com.lightstreamer.client.LightstreamerClient;
import com.psa.rxlightstreamer.exceptions.LightStreamerServerError;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.helpers.ServerError;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * <p>This class represents a LightStreamer client. The client has the option to connect or disconnect
 * from the service. Status updates and errors are sent via the available observer. </p>
 * <p>The class will also allow the user to subscribe to adapters.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxLightStreamerClient {
    /**
     * <p>It is a LightStreamer client from the LightStreamer API to handle the connections.</p>
     */
    @Inject
    LightstreamerClient mLightstreamerClient;
    /**
     * <p>This observable will wrap the listener so that it becomes an Observable.</p>
     */
    private Observable<ClientStatus> mClientStatusObservable;

    private List<RxSubscription> mSubscriptions;

    /**
     * <p>Upon instantiation, all dependencies are injected.</p>
     */
    public RxLightStreamerClient() {
        RxLightStreamerInjector.getRxLightStreamerComponent().inject(this);
        mSubscriptions = new ArrayList<>();
    }

    /**
     * <p>Establish a connection with LightStreamer.</p>
     * @param host is the URL of the host to connect to.
     * @param adapterSet is the adapter set name that is to be used.
     * @param user is the username to use
     * @param password is the password to use.
     */
    public void connect(String host, String adapterSet, String user, String password)
    {
        mLightstreamerClient.connectionDetails.setServerAddress(host);
        mLightstreamerClient.connectionDetails.setAdapterSet(adapterSet);
        mLightstreamerClient.connectionDetails.setUser(user);
        mLightstreamerClient.connectionDetails.setPassword(password);
        List<ClientListener> listeners = mLightstreamerClient.getListeners();
        for (ClientListener listener: listeners)
        {
            mLightstreamerClient.removeListener(listener);
        }
        mLightstreamerClient.connect();
        mClientStatusObservable = Observable.create(
                subscriber -> mLightstreamerClient.addListener(new ClientListener() {
                    @Override
                    public void onListenEnd(LightstreamerClient lightstreamerClient) {

                    }

                    @Override
                    public void onListenStart(LightstreamerClient lightstreamerClient) {

                    }

                    @Override
                    public void onServerError(int i, String s) {
                        subscriber.onError(new LightStreamerServerError(ServerError.fromLSCode(i), s));
                    }

                    @Override
                    public void onStatusChange(String s) {
                        subscriber.onNext(ClientStatus.fromLSStatus(s));
                    }

                    @Override
                    public void onPropertyChange(String s) {

                    }
                })
        );
    }
    /**
     * <p>Establish a connection with LightStreamer.</p>
     * @param host is the URL of the host to connect to.
     * @param adapterSet is the adapter set name that is to be used.
     */
    public void connect(String host, String adapterSet)
    {
        connect(host, adapterSet, null, null);
    }

    /**
     * <p>Ends a connection with LightStreamer.</p>
     */
    public void disconnect()
    {
        mLightstreamerClient.disconnect();
    }

    /**
     * <p>Returns the current status of the client.</p>
     * @return the connection status of the client.
     */
    public ClientStatus getStatus()
    {
        return ClientStatus.fromLSStatus(mLightstreamerClient.getStatus());
    }

    /**
     * <p>Returns an observable to send events to the affected class.</p>
     * @return Observable that sends connection status changes.
     */
    public Observable<ClientStatus> getClientStatusObservable() {
        return mClientStatusObservable;
    }

    //region Subscription methods

    /**
     * <p>Subscribes the given subscription.</p>
     * @param subscription subscription to subscribe on.
     */
    public void subscribe(RxSubscription subscription)
    {
        mLightstreamerClient.subscribe(subscription.getLSSubscription());
        mSubscriptions.add(subscription);
    }

    /**
     * <p>Unsubscribes the given subscription.</p>
     * @param subscription subscription to unsubscribe to.
     */
    public void unsubscribe(RxSubscription subscription)
    {
        if (mSubscriptions.contains(subscription))
        {
            mLightstreamerClient.unsubscribe(subscription.getLSSubscription());
            mSubscriptions.remove(subscription);
        }
    }

    /**
     * <p>Returns the number of subscriptions.</p>
     * @return the number of subscriptions.
     */
    public int getSubscriptionsCount()
    {
        return mSubscriptions.size();
    }

    /**
     * <p>Returns the subscription at the given position.</p>
     * @param position is the position.
     * @return the subscription at the given position.
     */
    public RxSubscription getSubscription(int position)
    {
        return mSubscriptions.get(position);
    }
    //endregion

    /**
     * <p>Inquires the connection options object for the current client.</p>
     * @return returns a connection options object.
     */
    public ConnectionOptions getConnectionOptions()
    {
        return mLightstreamerClient.connectionOptions;
    }
}
