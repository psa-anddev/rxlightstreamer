package com.psa.rxlightstreamer.core;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;
import com.psa.rxlightstreamer.exceptions.LightStreamerSubscriptionError;
import com.psa.rxlightstreamer.helpers.SubscriptionError;
import com.psa.rxlightstreamer.helpers.SubscriptionType;
import com.psa.rxlightstreamer.injection.RxLightStreamerComponent;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;

import rx.Observable;

/**
 * <p>This class represents a subscription. A subscription can be of one of the types of
 * {@link com.psa.rxlightstreamer.helpers.SubscriptionType} and will link with an adapter of the
 * adapter set given in the {@link RxLightStreamerClient}.</p>
 * <p>The class is meant to be extended in order to implement the items and the fields that
 * belong to the class as well as the conversion from {@link com.lightstreamer.client.ItemUpdate} to
 * the type that the subscription returns.</p>
 * <p>The updates from the subscription come from the subscription observable.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public abstract class RxSubscription<T> {
    //region Field declarations
    private Subscription mSubscription;
    private SubscriptionType mSubscriptionType;
    private String[] mFields, mItems;
    private String mAdapter;
    private boolean mSnapshot;
    protected Observable<SubscriptionEvent<ItemUpdate>> mRawObservable;
    //endregion

    /**
     * <p>Instantiates a RxSubscription.</p>
     * @param subscriptionType is the subscription type.
     * @param adapter is the adapter to subscribe.
     * @param fields is the fields that wnat to be subscribed.
     * @param items is the items to subscribe
     * @param snapshot if true, snapshot is on.
     */
    public RxSubscription(SubscriptionType subscriptionType, String adapter, String[] fields,
                          String[] items, boolean snapshot) {
        mSubscriptionType = subscriptionType;
        mAdapter = adapter;
        mFields = fields;
        mItems = items;
        mSnapshot = snapshot;
        RxLightStreamerComponent lsComponent = RxLightStreamerInjector.getRxLightStreamerComponent();
        switch (mSubscriptionType)
        {
            case MERGE:
                mSubscription = lsComponent.mergeSubscription();
                break;
            case RAW:
                mSubscription = lsComponent.rawSubscription();
                break;
            case DISTINCT:
                mSubscription = lsComponent.distinctSubscription();
                break;
            case COMMAND:
                mSubscription = lsComponent.commandSubscription();
                break;
        }
        mSubscription.setFields(mFields);
        mSubscription.setItems(mItems);
        mSubscription.setDataAdapter(mAdapter);
        mSubscription.setRequestedSnapshot(mSnapshot?"yes":"no");
        resetObservable();
    }

    //region Getters
    /**
     * <p>Returns the subscription type.</p>
     * @return the subscription type.
     */
    public SubscriptionType getSubscriptionType() {
        return mSubscriptionType;
    }

    /**
     * <p>Returns the fields that are to be received.</p>
     * @return a string array with the fields to be received.
     */
    public String[] getFields() {
        return mFields;
    }

    /**
     * <p>Returns the items that are to be subscribed.</p>
     * @return a string array with the items to be subscribed.
     */
    public String[] getItems() {
        return mItems;
    }

    /**
     * <p>Gets the adapter name for this subscription.</p>
     * @return the adapter name as a string.
     */
    public String getAdapter() {
        return mAdapter;
    }

    /**
     * <p>Returns whether the subscription uses snapshot or not.</p>
     * @return true when snapshot is used, false otherwise.
     */
    public boolean expectSnapshot() {
        return mSnapshot;
    }
    //endregion

    //region Abstract methods
    /**
     * <p>Returns the observable for the subscription.</p>
     * @return the observable that will send events for this subscription.
     */
    public abstract Observable<SubscriptionEvent<T>> getSubscriptionObservable();
    //endregion

    /**
     * <p>Resets the observable so that the subscription can be used again.</p>
     */
    private void resetObservable()
    {
        while (!mSubscription.getListeners().isEmpty())
            mSubscription.removeListener(mSubscription.getListeners().get(0));
        mRawObservable = Observable.create(subscriber -> mSubscription
                .addListener(new SubscriptionListener() {
            @Override
            public void onClearSnapshot(String s, int i) {

            }

            @Override
            public void onCommandSecondLevelItemLostUpdates(int i, String s) {

            }

            @Override
            public void onCommandSecondLevelSubscriptionError(int i, String s, String s1) {

            }

            @Override
            public void onEndOfSnapshot(String s, int i) {

            }

            @Override
            public void onItemLostUpdates(String s, int i, int i1) {

            }

            @Override
            public void onItemUpdate(ItemUpdate itemUpdate) {
                subscriber.onNext(new SubscriptionEvent<>(true, itemUpdate));
            }

            @Override
            public void onListenEnd(Subscription subscription) {

            }

            @Override
            public void onListenStart(Subscription subscription) {

            }

            @Override
            public void onSubscription() {
                subscriber.onNext(new SubscriptionEvent<ItemUpdate>(true, null));
            }

            @Override
            public void onSubscriptionError(int i, String s) {
                subscriber.onError(new LightStreamerSubscriptionError(SubscriptionError.fromLSCode(i), s));
            }

            @Override
            public void onUnsubscription() {
                subscriber.onNext(new SubscriptionEvent<ItemUpdate>(false, null));
                subscriber.onCompleted();
                resetObservable();
            }
        }));

    }

    /**
     * <p>Return the subscription so that it can be used by the client.</p>
     * @return the subscription object used.
     */
    Subscription getLSSubscription()
    {
        return mSubscription;
    }

    /**
     * <p>This class is a subscription event. The event will return if the object is
     * subscribed or not and an object of the given type. When that object is empty,
     * it means that the subscription has been subscribed or unsubscribed, depending on
     * the value of the is subscribed value.</p>
     * @param <T> is the type of the returned object.
     */
    public static class SubscriptionEvent<T>
    {
        private boolean mSubscribed;
        private T mUpdatedItem;

        /**
         * <p>Instantiates a new subscription event.</p>
         * @param subscribed true when subscribed.
         * @param updatedItem is the item updated.
         */
        public SubscriptionEvent(boolean subscribed, T updatedItem) {
            mSubscribed = subscribed;
            mUpdatedItem = updatedItem;
        }

        /**
         * <p>Returns whether the subscription is active or not.</p>
         * @return true when subscribed or false otherwise.
         */
        public boolean isSubscribed() {
            return mSubscribed;
        }

        /**
         * <p>Returns the last updated item.</p>
         * @return the last updated item or null to indicate that the subscription has become active
         * or inactive.
         */
        public T getUpdatedItem() {
            return mUpdatedItem;
        }
    }
}
