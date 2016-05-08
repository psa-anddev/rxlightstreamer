package com.psa.rxlightstreamer.sample.helpers;

import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.sample.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * <p>Feature: As a user I want to be able to send events to the LightStreamer service in the
 * application.</p>
 *
 * @author pablo
 * @version 1.0
 */
public class ServiceEventTest extends BaseTest
{
    private ServiceEvent mServiceEvent;
    private String mHost, mAdapterSet;
    @Mock
    private RxSubscription mUnifiedSubscription;
    @Mock
    private RxNonUnifiedSubscription mNonUnifiedSubscription;

    @Override
    public void setUp()
    {
        super.setUp();
        mHost = "Host";
        mAdapterSet = "AdapterSet";
        mServiceEvent = new ServiceEvent.Builder(ServiceEvent.EventType.DISCONNECT)
                .setLSHost(mHost)
                .setAdapterSet(mAdapterSet)
                .setUnifiedSubscription(mUnifiedSubscription)
                .setNonUnifiedSubscription(mNonUnifiedSubscription)
                .build();
    }

    /**
     * <p>Scenario: Service events are constructed as expected.</p>
     * <p>Given I am in the application
     * When I want to connect to LightStreamer
     * Then the event will be generated as expected.</p>
     */
    @Test
    public void testBuilderGeneratesRightServiceEvent()
    {
        try
        {
            assertThat(mServiceEvent).isNotNull();
            assertThat(mServiceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.DISCONNECT);
            assertThat(mServiceEvent.getLSHost()).isEqualTo(mHost);
            assertThat(mServiceEvent.getAdapterSet()).isEqualTo(mAdapterSet);
            assertThat(mServiceEvent.getSubscription()).isEqualTo(mUnifiedSubscription);
            assertThat(mServiceEvent.getNonUnifiedSubscription()).isEqualTo(mNonUnifiedSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
