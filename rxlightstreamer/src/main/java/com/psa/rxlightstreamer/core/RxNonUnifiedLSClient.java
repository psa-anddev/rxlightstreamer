package com.psa.rxlightstreamer.core;

import com.lightstreamer.ls_client.ConnectionInfo;
import com.lightstreamer.ls_client.ConnectionListener;
import com.lightstreamer.ls_client.LSClient;
import com.lightstreamer.ls_client.PushConnException;
import com.lightstreamer.ls_client.PushServerException;
import com.lightstreamer.ls_client.PushUserException;
import com.lightstreamer.ls_client.SubscrException;
import com.psa.rxlightstreamer.exceptions.LightStreamerServerError;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.helpers.ServerError;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * <p>This class provides a way to use LightStreamer service using the non unified API.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxNonUnifiedLSClient {
    //region Injected objects.
    @Inject
    LSClient mLSClient;
    @Inject
    ConnectionInfo mConnectionInfo;
    //endregion

    //region Private fields
    private Observable<ClientStatus> mClientStatusObservable;
    private ClientStatus mClientStatus, mPreviousClientStatus;
    private ConnectionListener mConnectionListener;
    private List<RxNonUnifiedSubscription> mSubscriptions;
    //endregion

    //region Public methods

    /**
     * <p>Establishes a connection with LightStreamer.</p>
     * @param host is the host to connect to.
     * @param adapterSet is the adapter set to connect to.
     * @param user is the user name of use.
     * @param password is the password for that user.
     */
    public void connect(String host, String adapterSet, String user, String password)
    {
        mClientStatusObservable = Observable.create(subscriber -> {
            mConnectionInfo.pushServerUrl = host;
            mConnectionInfo.adapter = adapterSet;
            if (user != null)
                mConnectionInfo.user = user;
            if (password != null)
                mConnectionInfo.password = password;
            try {
                mConnectionListener = new ConnectionListener() {
                    @Override
                    public void onConnectionEstablished() {
                        mClientStatus = ClientStatus.STREAM_SENSING;
                        subscriber.onNext(mClientStatus);
                    }

                    @Override
                    public void onSessionStarted(boolean b) {
                        if (b) //Is polling
                            mClientStatus = ClientStatus.HTTP_POLLING;
                        else
                            mClientStatus = ClientStatus.HTTP_STREAMING;
                        subscriber.onNext(mClientStatus);
                    }

                    @Override
                    public void onNewBytes(long l) {
                        //Do nothing.
                    }

                    @Override
                    public void onDataError(PushServerException e) {
                        subscriber.onError(new LightStreamerServerError(ServerError.fromLSCode(e.getErrorCode()), e.getMessage()));
                    }

                    @Override
                    public void onActivityWarning(boolean b) {
                        if (b) //Status is stalled
                        {
                            mPreviousClientStatus = mClientStatus;
                            mClientStatus = ClientStatus.STALLED;
                        }
                        else
                            mClientStatus = mPreviousClientStatus;
                        subscriber.onNext(mClientStatus);
                    }

                    @Override
                    public void onClose() {
                        mClientStatus = ClientStatus.DISCONNECTED;
                        subscriber.onNext(mClientStatus);
                    }

                    @Override
                    public void onEnd(int i) {
                        mClientStatus = ClientStatus.WILL_RETRY;
                        subscriber.onNext(mClientStatus);
                    }

                    @Override
                    public void onFailure(PushServerException e) {
                        subscriber.onError(new LightStreamerServerError(ServerError.fromLSCode(e.getErrorCode()), e.getMessage()));
                    }

                    @Override
                    public void onFailure(PushConnException e) {
                        subscriber.onError(e);
                    }
                };
                mLSClient.openConnection(mConnectionInfo, mConnectionListener);
                mClientStatus = ClientStatus.CONNECTING;
                subscriber.onNext(mClientStatus);
            } catch (PushConnException e) {
                subscriber.onError(e);
            } catch (PushServerException e) {
                subscriber.onError(new LightStreamerServerError(ServerError.fromLSCode(e.getErrorCode()), e.getMessage()));
            } catch (PushUserException e) {
                subscriber.onError(new LightStreamerServerError(ServerError.fromLSCode(e.getErrorCode()), e.getMessage()));
            }
        });
    }

    /**
     * <p>Establishes a connection with LightStreamer.</p>
     * @param host is the host to connect to.
     * @param adapterSet is the adapter set to connect to.
     */
    public void connect (String host, String adapterSet)
    {
        connect(host, adapterSet, null, null);
    }

    /**
     * <p>Disconnects the client from LightStreamer.</p>
     */
    public void disconnect()
    {
        mLSClient.closeConnection();
    }

    /**
     * <p>Returns the connection status of LightStreamer.</p>
     * @return a client status.
     */
    public ClientStatus getStatus()
    {
        return mClientStatus;
    }

    /**
     * <p>Returns the observable to listen to status events.</p>
     * @return a client status observable.
     */
    public Observable<ClientStatus> getClientStatusObservable()
    {
        return mClientStatusObservable;
    }

    public ConnectionListener getListener()
    {
        return mConnectionListener;
    }

    public void subscribe(RxNonUnifiedSubscription subscription) throws SubscrException, PushConnException, PushUserException, PushServerException {
        subscription.setSubscribedTableKey(mLSClient.subscribeTable(subscription.getExtendedTableInfo(), subscription.getHandyTableListener(), false));
        mSubscriptions.add(subscription);

    }

    public void unsubscribe(RxNonUnifiedSubscription subscription) throws PushConnException, PushServerException, SubscrException {
        mLSClient.unsubscribeTable(subscription.getSubscribedTableKey());
        mSubscriptions.remove(subscription);
    }

    public int getSubscriptionsCount()
    {
        return mSubscriptions.size();
    }

    public RxNonUnifiedSubscription getSubscription(int position)
    {
        return mSubscriptions.get(position);
    }
    //endregion

    //region Constructors

    /**
     * <p>Instantiates a non unified LightStreamer client.</p>
     */
    public RxNonUnifiedLSClient() {
        RxLightStreamerInjector.getRxLightStreamerComponent().inject(this);
        mClientStatus = ClientStatus.DISCONNECTED;
        mSubscriptions = new ArrayList<>();
    }

    //endregion
}
