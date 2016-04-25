package com.psa.rxlightstreamer.injection;

import com.lightstreamer.client.Subscription;
import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.core.RxSubscription;

import javax.inject.Named;

import dagger.Component;

/**
 * <p>Injects RxLightStreamer dependencies.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
@Component(modules = {CoreModule.class})
public interface RxLightStreamerComponent {
    void inject(RxLightStreamerClient rxLightStreamerClient);
    @Named("MERGE")
    Subscription mergeSubscription();
    @Named("DISTINCT")
    Subscription distinctSubscription();
    @Named("RAW")
    Subscription rawSubscription();
    @Named("COMMAND")
    Subscription commandSubscription();
}
