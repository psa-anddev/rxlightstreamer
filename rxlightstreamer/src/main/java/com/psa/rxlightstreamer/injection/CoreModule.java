package com.psa.rxlightstreamer.injection;

import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.ls_client.ConnectionInfo;
import com.lightstreamer.ls_client.LSClient;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * <p>Provides instances for the core elements of LighStreamer.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
@Module
public class CoreModule {
    /**
     * <p>Provides an instance of LightStreamer Client.</p>
     * @return a new instance of LightStreamer Client.
     */
    @Provides
    public LightstreamerClient provideLightStreamerClient()
    {
        return new LightstreamerClient(null, null);
    }

    /**
     * <p>Provides a merge subscription.</p>
     * @return a merge subscription
     */
    @Provides @Named("MERGE")
    public Subscription provideMergeSubscription()
    {
        return new Subscription("MERGE");
    }

    /**
     * <p>Provides a command subscription.</p>
     * @return a merge subscription
     */
    @Provides @Named("COMMAND")
    public Subscription provideCommandSubscription()
    {
        return new Subscription("COMMAND");
    }

    /**
     * <p>Provides a distinct subscription.</p>
     * @return a distinct subscription
     */
    @Provides @Named("DISTINCT")
    public Subscription provideDistinctSubscription()
    {
        return new Subscription("DISTINCT");
    }

    /**
     * <p>Provides a raw subscription.</p>
     * @return a raw subscription
     */
    @Provides @Named("RAW")
    public Subscription provideRawSubscription()
    {
        return new Subscription("RAW");
    }

    /**
     * <p>Provides an instance of the non unified version of LightStreamer client.</p>
     * @return an instance of {@link LSClient}
     */
    @Provides
    public LSClient provideNonUnifiedRawClient()
    {
        return new LSClient();
    }

    /**
     * <p>Provides a connection information object to use to provide the connection data to the
     * non unified LightStreamer Client.</p>
     * @return a {@link ConnectionInfo} object.
     */
    @Provides
    public ConnectionInfo provideConnectionInfo() {
        return new ConnectionInfo();
    }
}
