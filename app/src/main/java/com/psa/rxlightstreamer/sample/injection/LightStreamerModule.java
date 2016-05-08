package com.psa.rxlightstreamer.sample.injection;

import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedLSClient;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteNonUnifiedSubscription;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * <p>This module provides all the LightStreamer objects.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
@Module
public class LightStreamerModule {
    private ServiceMediator mServiceMediator;
    private RxLightStreamerClient mRxLightStreamerClient;
    /**
     * <p>Returns the service mediator object.</p>
     * @return the service mediator object.
     */
    @Provides @Singleton
    public ServiceMediator provideServiceMediator()
    {
        if (mServiceMediator == null)
            mServiceMediator = new ServiceMediator();
        return mServiceMediator;
    }

    /**
     * <p>Returns an instance of the reactive LS client.</p>
     * @return an instance of RxLightStreamerClient.
     */
    @Provides @Singleton
    public RxLightStreamerClient provideLightStreamerClient()
    {
        if (mRxLightStreamerClient == null)
            mRxLightStreamerClient = new RxLightStreamerClient();
        return mRxLightStreamerClient;
    }

    /**
     * <p>Provides a quote subscription.</p>
     * @return Quote subscription.
     */
    @Provides @Singleton
    public QuoteSubscription provideQuoteSubscription()
    {
        return new QuoteSubscription();
    }

    /**
     * <p>Provides a non unified LightStreamer client.</p>
     * @return a non unified LightStreamer client.
     */
    @Provides @Singleton
    public RxNonUnifiedLSClient provideNonUnifiedClient() {
        return new RxNonUnifiedLSClient();
    }

    /**
     * <p>Returns a quote non unified subscription.</p>
     * @return a quote subscription.
     */
    @Provides @Singleton
    public QuoteNonUnifiedSubscription provideQuoteNonUnifiedSubscription()
    {
        return new QuoteNonUnifiedSubscription();
    }
}
