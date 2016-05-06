package com.psa.rxlightstreamer.injection;

import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.ls_client.ConnectionInfo;
import com.lightstreamer.ls_client.LSClient;

/**
 * <p>This is the core module for tests.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class TestCoreModule extends CoreModule {
    /**
     * <p>This variable will store the test dependency to use.</p>
     */
    private LightstreamerClient mLightstreamerClient;
    private Subscription mMergeSubscription, mDistinctSubscription, mRawSubscription, mCommandSubscription;
    private LSClient mLSClient;
    private ConnectionInfo mConnectionInfo;

    //region Providers
    @Override
    public LightstreamerClient provideLightStreamerClient() {
        //The method is overriden so that if the developer has defined an
        //object to inject, that object will be inject, the normal course of
        //action will happen if the object is not assigned.
        if (mLightstreamerClient == null)
            return super.provideLightStreamerClient();
        else
            return mLightstreamerClient;
    }

    @Override
    public Subscription provideMergeSubscription() {
        if (mMergeSubscription == null)
            return super.provideMergeSubscription();
        else
            return mMergeSubscription;
    }

    @Override
    public Subscription provideCommandSubscription() {
        if (mCommandSubscription == null)
            return super.provideCommandSubscription();
        else
            return mCommandSubscription;
    }

    @Override
    public Subscription provideDistinctSubscription() {
        if (mDistinctSubscription == null)
            return super.provideDistinctSubscription();
        else
            return mDistinctSubscription;
    }

    @Override
    public Subscription provideRawSubscription() {
        if (mRawSubscription == null)
            return super.provideRawSubscription();
        else
            return mRawSubscription;
    }

    @Override
    public LSClient provideNonUnifiedRawClient() {
        if (mLSClient == null)
            return super.provideNonUnifiedRawClient();
        else
            return mLSClient;
    }

    @Override
    public ConnectionInfo provideConnectionInfo() {
        if (mConnectionInfo == null)
            return super.provideConnectionInfo();
        else
            return mConnectionInfo;
    }

    //endregion

    //region Module setters
    /**
     * <p>Assigns a mock dependency to return as the LightStreamer client object.</p>
     * @param lightstreamerClient is the LightStreamer client to use.
     */
    public void setLightstreamerClient(LightstreamerClient lightstreamerClient) {
        mLightstreamerClient = lightstreamerClient;
    }

    /**
     * <p>Sets the merge subscription for testing.</p>
     * @param mergeSubscription merge subscription to use in the tests.
     */
    public void setMergeSubscription(Subscription mergeSubscription) {
        mMergeSubscription = mergeSubscription;
    }

    /**
     * <p>Sets the distinct subscription for tests.</p>
     * @param distinctSubscription is the distinct subscription to use.
     */
    public void setDistinctSubscription(Subscription distinctSubscription) {
        mDistinctSubscription = distinctSubscription;
    }

    /**
     * <p>Sets the raw subscription for tests.</p>
     * @param rawSubscription is the raw subscription to use.
     */
    public void setRawSubscription(Subscription rawSubscription) {
        mRawSubscription = rawSubscription;
    }

    /**
     * <p>Sets the command subscription for tests.</p>
     * @param commandSubscription command subscription to use.
     */
    public void setCommandSubscription(Subscription commandSubscription) {
        mCommandSubscription = commandSubscription;
    }

    /**
     * <p>Sets a client for testing purposes.</p>
     * @param LSClient a client for testing purposes.
     */
    public void setLSClient(LSClient LSClient) {
        mLSClient = LSClient;
    }

    /**
     * <p>Sets a connection information object for testing purposes.</p>
     * @param connectionInfo the connection information object to use.
     */
    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        mConnectionInfo = connectionInfo;
    }

    //endregion
}
