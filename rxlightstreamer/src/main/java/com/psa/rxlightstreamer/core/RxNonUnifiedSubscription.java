package com.psa.rxlightstreamer.core;

import com.lightstreamer.ls_client.ExtendedTableInfo;
import com.lightstreamer.ls_client.HandyTableListener;
import com.lightstreamer.ls_client.SubscrException;
import com.lightstreamer.ls_client.SubscribedTableKey;
import com.lightstreamer.ls_client.UpdateInfo;
import com.psa.rxlightstreamer.helpers.SubscriptionType;

import rx.Observable;

/**
 * <p>This class represents a subscription for using with the non-unified API.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public abstract class RxNonUnifiedSubscription<T> {
    //region Private fields
    private SubscriptionType mSubscriptionType;
    private String[] mFields, mItems;
    private String mAdapter;
    private boolean mSnapshot;
    protected Observable<RxSubscription.SubscriptionEvent<UpdateInfo>> mRawObservable;
    private ExtendedTableInfo mExtendedTableInfo;
    private HandyTableListener mHandyTableListener;
    private SubscribedTableKey mSubscribedTableKey;

    //endregion

    //region Constructors

    /**
     * <p>Instantiates a subscription.</p>
     * @param type is the type.
     * @param adapter the adapter name.
     * @param fields the fields.
     * @param items the items.
     * @param expectSnapshot true if snapshot is expected.
     */
    public RxNonUnifiedSubscription(SubscriptionType type, String adapter, String[] fields, String[] items, boolean expectSnapshot)
    {
        mSubscriptionType = type;
        mAdapter = adapter;
        mFields = fields;
        mItems = items;
        mSnapshot = expectSnapshot;
        resetObservable();
    }
    //endregion

    //region Getters

    /**
     * <p>Gets the subscription type.</p>
     * @return subscription type.
     */
    public SubscriptionType getSubscriptionType() {
        return mSubscriptionType;
    }

    /**
     * <p>Returns the fields of the subscription.</p>
     * @return the fields of the subscription.
     */
    public String[] getFields() {
        return mFields;
    }

    /**
     * <p>Returns the items.</p>
     * @return items of the subscription.
     */
    public String[] getItems() {
        return mItems;
    }

    /**
     * <p>Returns the adapter in use.</p>
     * @return adapter the subscription is subscribing to.
     */
    public String getAdapter() {
        return mAdapter;
    }

    /**
     * <p>Returns whether the subscription allows snapshot or not.</p>
     * @return true when snapshot.
     */
    public boolean isSnapshot() {
        return mSnapshot;
    }

    /**
     * <p>Get the data to establish the subscription.</p>
     * @return data to establish the subscription.
     */
    public ExtendedTableInfo getExtendedTableInfo() {
        return mExtendedTableInfo;
    }

    /**
     * <p>Returns the listener to pass to the LS client.</p>
     * @return subscription raw listener.
     */
    public HandyTableListener getHandyTableListener() {
        return mHandyTableListener;
    }

    /**
     * <p>Returns the subscribed table key.</p>
     * @return the subscribed table key.
     */
    public SubscribedTableKey getSubscribedTableKey() {
        return mSubscribedTableKey;
    }

    /**
     * <p>Returns the observable that will give updates.</p>
     * @return observable of the subscription.
     */
    public abstract Observable<RxSubscription.SubscriptionEvent<T>> getSubscriptionObservable();
    //endregion

    //region Setters

    /**
     * <p>Set the subscribed table key.</p>
     * @param subscribedTableKey the subscribed table key.
     */
    public void setSubscribedTableKey(SubscribedTableKey subscribedTableKey) {
        mSubscribedTableKey = subscribedTableKey;
    }

    //endregion

    //region Private helper methods

    /**
     * <p>Resets the raw observable.</p>
     */
    private void resetObservable()
    {
        mRawObservable = Observable.create(subscriber -> {
            try {
                mExtendedTableInfo = new ExtendedTableInfo(mItems, mSubscriptionType.getLSSubscriptionType(), mFields, mSnapshot);
                mExtendedTableInfo.setDataAdapter(mAdapter);
                mHandyTableListener = new HandyTableListener() {
                    @Override
                    public void onUpdate(int i, String s, UpdateInfo updateInfo) {
                        subscriber.onNext(new RxSubscription.SubscriptionEvent<>(true, updateInfo));
                    }

                    @Override
                    public void onSnapshotEnd(int i, String s) {

                    }

                    @Override
                    public void onRawUpdatesLost(int i, String s, int i1) {

                    }

                    @Override
                    public void onUnsubscr(int i, String s) {

                    }

                    @Override
                    public void onUnsubscrAll() {
                        subscriber.onNext(new RxSubscription.SubscriptionEvent<UpdateInfo>(false, null));
                        subscriber.onCompleted();
                        resetObservable();
                    }
                };
                subscriber.onNext(new RxSubscription.SubscriptionEvent<UpdateInfo>(true, null));
            } catch (SubscrException e) {
                subscriber.onError(e);
            }
        });
    }
    //endregion
}
