package com.psa.rxlightstreamer.sample.helpers;

import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;

/**
 * <p>This class represents an event to be sent to the service which will perform an action in the
 * LS connection such as connecting or disconnecting LS or subscribing or unsubscribing adapters.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class ServiceEvent
{
    /**
     * <p>Represents the event type.</p>
     */
    public enum EventType
    {
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
    private RxNonUnifiedSubscription mNonUnifiedSubscription;

    /**
     * <p>Instantiates a service event.</p>
     *
     * @param eventType              is the event type.
     * @param subscription           is a subscription.
     * @param nonUnifiedSubscription is a non unified subscription
     * @param LSHost                 is the LS host.
     * @param adapterSet             is the adapter set to use.
     */
    private ServiceEvent(EventType eventType, RxSubscription subscription, RxNonUnifiedSubscription nonUnifiedSubscription, String LSHost, String adapterSet)
    {
        mEventType = eventType;
        mSubscription = subscription;
        mNonUnifiedSubscription = nonUnifiedSubscription;
        mLSHost = LSHost;
        mAdapterSet = adapterSet;
    }

    /**
     * <p>Returns the event type.</p>
     *
     * @return the event type.
     */
    public EventType getEventType()
    {
        return mEventType;
    }

    /**
     * <p>Returns the subscription.</p>
     *
     * @return subscription to use.
     */
    public RxSubscription getSubscription()
    {
        return mSubscription;
    }

    /**
     * <p>Returns the LS host.</p>
     *
     * @return the LS host.
     */
    public String getLSHost()
    {
        return mLSHost;
    }

    /**
     * <p>Returns the adapter set.</p>
     *
     * @return the adapter set to use.
     */
    public String getAdapterSet()
    {
        return mAdapterSet;
    }

    /**
     * <p>Returns the non unified subscription.</p>
     *
     * @return a non unified subscription.
     */
    public RxNonUnifiedSubscription getNonUnifiedSubscription()
    {
        return mNonUnifiedSubscription;
    }

    /**
     * <p>This class builds new service events.</p>
     */
    public static class Builder
    {
        private EventType mEventType;
        private String mLSHost;
        private String mAdapterSet;
        private RxNonUnifiedSubscription mNonUnifiedSubscription;
        private RxSubscription mUnifiedSubscription;

        /**
         * <p>Instantiates the builder.</p>
         *
         * @param eventType is the event type to set.
         */
        public Builder(EventType eventType)
        {
            mEventType = eventType;
        }

        /**
         * <p>Sets the LightStreamer host.</p>
         *
         * @param LSHost is the LightStreamer host to set.
         * @return the builder.
         */
        public Builder setLSHost(String LSHost)
        {
            mLSHost = LSHost;
            return this;
        }

        /**
         * <p>Sets the adapter set.</p>
         *
         * @param adapterSet the adapter set to use with LightStreamer.
         * @return the builder.
         */
        public Builder setAdapterSet(String adapterSet)
        {
            mAdapterSet = adapterSet;
            return this;
        }

        /**
         * <p>Sets a non unified subscription.</p>
         *
         * @param nonUnifiedSubscription non unified subscription to use.
         */
        public Builder setNonUnifiedSubscription(RxNonUnifiedSubscription nonUnifiedSubscription)
        {
            mNonUnifiedSubscription = nonUnifiedSubscription;
            return this;
        }

        /**
         * <p>Sets a unified subscription.</p>
         *
         * @param unifiedSubscription unified subscription to use.
         * @return the builder.
         */
        public Builder setUnifiedSubscription(RxSubscription unifiedSubscription)
        {
            mUnifiedSubscription = unifiedSubscription;
            return this;
        }

        /**
         * <p>Builds the object.</p>
         *
         * @return the service event.
         */
        public ServiceEvent build()
        {
            return new ServiceEvent(mEventType, mUnifiedSubscription, mNonUnifiedSubscription,
                    mLSHost, mAdapterSet);
        }
    }
}
