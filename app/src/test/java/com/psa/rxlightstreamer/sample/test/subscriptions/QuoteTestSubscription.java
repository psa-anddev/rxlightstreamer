package com.psa.rxlightstreamer.sample.test.subscriptions;

import com.lightstreamer.client.ItemUpdate;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

import rx.Observable;


/**
 * <p>This is a quote test subscription that allows to set a mock observable, making the subscription
 * testable.</p>
 */
public class QuoteTestSubscription extends QuoteSubscription {
    /**
     * <p>Sets a test raw observable.</p>
     * @param rawObservable is the new test observable.
     */
    public void setRawObservable(Observable<SubscriptionEvent<ItemUpdate>> rawObservable)
    {
        mRawObservable = rawObservable;
    }
}
