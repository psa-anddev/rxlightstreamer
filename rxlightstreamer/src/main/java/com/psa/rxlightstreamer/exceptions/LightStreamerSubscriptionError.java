package com.psa.rxlightstreamer.exceptions;

import com.psa.rxlightstreamer.helpers.SubscriptionError;

/**
 * <p>This exception is sent as an error through the subscription observable. The exception contains
 * the subscription error as well as the message returned by the server.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class LightStreamerSubscriptionError extends Exception {
    private SubscriptionError mSubscriptionError;

    /**
     * <p>Instantiate a LightStreamer subscription error.</p>
     * @param subscriptionError is the subscription error.
     * @param detailMessage is the message that LS returned.
     */
    public LightStreamerSubscriptionError(SubscriptionError subscriptionError, String detailMessage) {
        super(detailMessage);
        mSubscriptionError = subscriptionError;
    }

    /**
     * <p>Returns the subscription error that thrown the exception.</p>
     * @return subscription error associated with the exception.
     */
    public SubscriptionError getSubscriptionError() {
        return mSubscriptionError;
    }
}
