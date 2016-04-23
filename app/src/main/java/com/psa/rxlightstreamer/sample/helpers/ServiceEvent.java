package com.psa.rxlightstreamer.sample.helpers;

import com.psa.rxlightstreamer.core.RxSubscription;

/**
 * <p>This class represents an event to be sent to the service which will perform an action in the
 * LS connection such as connecting or disconnecting LS or subscribing or unsubscribing adapters.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class ServiceEvent {
    /**
     * <p>Represents the event type.</p>
     */
    public enum EventType {
        /**
         * <p>Connects LS.</p>
         */
        CONNECT,
        /**
         * <p>Disconnect LS.</p>
         */
        DISCONNECT,
        /**
         * <p>Subscribes an adapter.</p>
         */
        SUBSCRIBE,
        /**
         * <p>Unsubscribes an adapter.</p>
         */
        UNSUBSCRIBE
    }

    private EventType mEventType;
    private RxSubscription mSubscription;
    private String mLSHost;
    private String mAdapterSet;

    /**
     * <p>Instantiates a service event.</p>
     * @param eventType is the event type.
     * @param subscription is a subscription.
     * @param LSHost is the LS host.
     * @param adapterSet is the adapter set to use.
     */
    public ServiceEvent(EventType eventType, RxSubscription subscription, String LSHost, String adapterSet) {
        mEventType = eventType;
        mSubscription = subscription;
        mLSHost = LSHost;
        mAdapterSet = adapterSet;
    }

    /**
     * <p>Returns the event type.</p>
     * @return the event type.
     */
    public EventType getEventType() {
        return mEventType;
    }

    /**
     * <p>Returns the subscription.</p>
     * @return subscription to use.
     */
    public RxSubscription getSubscription() {
        return mSubscription;
    }

    /**
     * <p>Returns the LS host.</p>
     * @return the LS host.
     */
    public String getLSHost() {
        return mLSHost;
    }

    /**
     * <p>Returns the adapter set.</p>
     * @return the adapter set to use.
     */
    public String getAdapterSet() {
        return mAdapterSet;
    }
}
