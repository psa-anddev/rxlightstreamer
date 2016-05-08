package com.psa.rxlightstreamer.sample.injection;

import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedLSClient;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteNonUnifiedSubscription;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

/**
 * <p>This module extends the LightStreamer module in order to provide mock objects for testing
 * purposes.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class TestLightStreamerModule extends LightStreamerModule {
    private ServiceMediator mServiceMediator;
    private RxLightStreamerClient mRxLightStreamerClient;
    private QuoteSubscription mQuoteSubscription;
    private RxNonUnifiedLSClient mNonUnifiedLSClient;
    private QuoteNonUnifiedSubscription mQuoteNonUnifiedSubscription;

    //region Overriden methods
    @Override
    public ServiceMediator provideServiceMediator() {
        if (mServiceMediator == null)
            return super.provideServiceMediator();
        else
            return mServiceMediator;
    }

    @Override
    public RxLightStreamerClient provideLightStreamerClient() {
        if (mRxLightStreamerClient == null)
            return super.provideLightStreamerClient();
        else
            return mRxLightStreamerClient;
    }

    @Override
    public QuoteSubscription provideQuoteSubscription() {
        if (mQuoteSubscription == null)
            return super.provideQuoteSubscription();
        else
            return mQuoteSubscription;
    }

    @Override
    public RxNonUnifiedLSClient provideNonUnifiedClient()
    {
        if (mNonUnifiedLSClient == null)
            return super.provideNonUnifiedClient();
        else
            return mNonUnifiedLSClient;
    }

    @Override
    public QuoteNonUnifiedSubscription provideQuoteNonUnifiedSubscription()
    {
        if (mQuoteNonUnifiedSubscription == null)
            return super.provideQuoteNonUnifiedSubscription();
        else
            return mQuoteNonUnifiedSubscription;
    }

    //endregion

    //region Setters

    /**
     * <p>Set a service mediator for testing purposes.</p>
     * @param serviceMediator is the mediator to use in the tests.
     */
    public void setServiceMediator(ServiceMediator serviceMediator) {
        mServiceMediator = serviceMediator;
    }

    /**
     * <p>Sets the LightStreamer client to use.</p>
     * @param rxLightStreamerClient is the ls client to use.
     */
    public void setRxLightStreamerClient(RxLightStreamerClient rxLightStreamerClient) {
        mRxLightStreamerClient = rxLightStreamerClient;
    }

    /**
     * <p>Sets teh Quote subscription to use.</p>
     * @param quoteSubscription is the quote subscription to use.
     */
    public void setQuoteSubscription(QuoteSubscription quoteSubscription) {
        mQuoteSubscription = quoteSubscription;
    }

    /**
     * <p>Sets the non unified client to use in the tests.</p>
     * @param nonUnifiedLSClient non unified client to use in the tests.
     */
    public void setNonUnifiedLSClient(RxNonUnifiedLSClient nonUnifiedLSClient)
    {
        mNonUnifiedLSClient = nonUnifiedLSClient;
    }

    /**
     * <p>Sets the non unified quote subscription for testing.</p>
     * @param quoteNonUnifiedSubscription Quote subscription for testing.
     */
    public void setQuoteNonUnifiedSubscription(QuoteNonUnifiedSubscription quoteNonUnifiedSubscription)
    {
        mQuoteNonUnifiedSubscription = quoteNonUnifiedSubscription;
    }

    //endregion
}
