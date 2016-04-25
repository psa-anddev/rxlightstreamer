package com.psa.rxlightstreamer.injection;

/**
 * <p>Provides the components used in RxLightStreamer.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxLightStreamerInjector {
    /**
     * <p>This component injects LightStreamer dependencies.</p>
     */
    private static RxLightStreamerComponent mRxLightStreamerComponent;

    /**
     * <p>Returns the RxLightStreamer component.</p>
     * @return the RxLightStreamer component in use.
     */
    public static RxLightStreamerComponent getRxLightStreamerComponent()
    {
        if (mRxLightStreamerComponent == null)
            mRxLightStreamerComponent = DaggerRxLightStreamerComponent.create();
        return mRxLightStreamerComponent;
    }

    /**
     * <p>Sets the rxLightStreamer component for testing.</p>
     * @param rxLightStreamerComponent the component to set.
     */
    public static void setRxLightStreamerComponent(RxLightStreamerComponent rxLightStreamerComponent)
    {
        mRxLightStreamerComponent = rxLightStreamerComponent;
    }
}
