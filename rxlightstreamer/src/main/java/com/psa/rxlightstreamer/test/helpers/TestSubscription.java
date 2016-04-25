package com.psa.rxlightstreamer.test.helpers;

import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.SubscriptionType;

import rx.Observable;

/**
 * <p>RxSubscription for tests.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class TestSubscription extends RxSubscription<String> {
    private static String mAdapter = "DEMO";
    private static String[] mFields = {"FIELD"}, mItems = {"ITEM"};

    /**
     * <p>Instantiates a RxSubscription.</p>
     */
    public TestSubscription() {
        super(SubscriptionType.MERGE, mAdapter, mFields, mItems, true);
    }

    @Override
    public Observable<SubscriptionEvent<String>> getSubscriptionObservable() {
        return mRawObservable
                .map(rawEvent ->
                        new SubscriptionEvent<>(rawEvent.isSubscribed(),
                                rawEvent.getUpdatedItem() == null?null:rawEvent.getUpdatedItem().getValue(0)));
    }
}
