package com.psa.rxlightstreamer.core;

import com.lightstreamer.client.ClientListener;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.exceptions.LightStreamerServerError;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.helpers.ServerError;
import com.psa.rxlightstreamer.injection.DaggerRxLightStreamerComponent;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;
import com.psa.rxlightstreamer.injection.TestCoreModule;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Feature: As a user I want to be able to connect, disconnect and know the status of my
 * LightStreamer connection as well as add and remove subscriptions.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class RxLightStreamerClientTest extends BaseTest {
    @Spy
    private LightstreamerClient mLightstreamerClient;
    @Mock
    private ClientListener mClientListener;

    @Mock
    private RxSubscription mRxSubscription;
    @Mock
    private Subscription mLSSubscription;

    private RxLightStreamerClient mRxLightStreamerClient;

    @Override
    public void setUp() {
        mLightstreamerClient = new LightstreamerClient(null, null);
        super.setUp();
        TestCoreModule testCoreModule = new TestCoreModule();
        testCoreModule.setLightstreamerClient(mLightstreamerClient);
        RxLightStreamerInjector.setRxLightStreamerComponent(DaggerRxLightStreamerComponent.builder()
        .coreModule(testCoreModule).build());
        mRxLightStreamerClient = new RxLightStreamerClient();
    }

    /**
     * <p>Scenario: Light Streamer client can connect.</p>
     * <p>Given I am in the application
     * When I want to connect to LightStreamer using Rx
     * Then the underlying API is called to handle it.</p>
     */
    @Test
    public void testLightStreamerClientIsConnectedWhenConnectMethodIsCalledOnTheRxObject()
    {
        try {
            List<ClientListener> clientListeners = new ArrayList<>();
            clientListeners.add(mClientListener);
            doNothing().when(mLightstreamerClient).connect();
            doReturn(clientListeners).when(mLightstreamerClient).getListeners();

            mRxLightStreamerClient.connect("http://localhost:8081", "Demo", "user", "password");

            assertThat(mRxLightStreamerClient.getClientStatusObservable()).isNotNull();

            verify(mLightstreamerClient).getListeners();
            verify(mLightstreamerClient).removeListener(mClientListener);
            verify(mLightstreamerClient).connect();
            verifyNoMoreInteractions(mLightstreamerClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: LightStreamerClient can disconnect.</p>
     * <p>Given I am in the application
     * When I want to disconnect from LightStreamer
     * Then LightStreamer is disconnected without any problems.</p>
     */
    @Test
    public void testLightStreamerClientIsDisconnectedWhenDisconnectMethodIsCalledOnTheRxObject()
    {
        try {
            mRxLightStreamerClient.disconnect();
            verify(mLightstreamerClient).disconnect();
            verifyNoMoreInteractions(mLightstreamerClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Checking connection status.</p>
     * <p>Given I am using LightStreamer
     * When I want to get the connection status
     * Then I get a representation that the library understands.</p>
     */
    @Test
    public void testLightStreamerClientCanReturnTheCurrentStatusWhenMethodIsCalledInTheRxObject()
    {
        try {
            when(mLightstreamerClient.getStatus()).thenReturn("CONNECTED:WS-STREAMING");
            assertThat(mRxLightStreamerClient.getStatus()).isEqualTo(ClientStatus.WS_STREAMING);
            verify(mLightstreamerClient).getStatus();
            verifyNoMoreInteractions(mLightstreamerClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: I receive status updates from LightStreamer</p>
     * <p>Given I am connected to LightStreamer
     * When my connection status changes
     * Then I receive an event through the observable.</p>
     */
    @Test
    public void testObserverNotifiesStatusUpdates()
    {
        try {
            doNothing().when(mLightstreamerClient).connect();

            TestSubscriber<ClientStatus> testSubscriber = new TestSubscriber<>();
            mRxLightStreamerClient.connect("http://localhost:8080", "Demo", "user", "password");
            mRxLightStreamerClient.getClientStatusObservable().subscribe(testSubscriber);
            mLightstreamerClient.getListeners().get(0).onStatusChange(ClientStatus.CONNECTING.getLightStreamerStatus());
            mLightstreamerClient.getListeners().get(0).onStatusChange(ClientStatus.STREAM_SENSING.getLightStreamerStatus());
            mLightstreamerClient.getListeners().get(0).onStatusChange(ClientStatus.WS_STREAMING.getLightStreamerStatus());

            List<ClientStatus> updates = testSubscriber.getOnNextEvents();
            assertThat(updates).isNotNull();
            assertThat(updates).isNotEmpty();
            assertThat(updates).hasSize(3);
            assertThat(updates.get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(updates.get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(updates.get(2)).isEqualTo(ClientStatus.WS_STREAMING);

            verify(mLightstreamerClient).connect();
            verify(mLightstreamerClient).addListener(any());
            verify(mLightstreamerClient, times(4)).getListeners();
            verifyNoMoreInteractions(mLightstreamerClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: I receive server errors from LightStreamer</p>
     * <p>Given I am connected to LightStreamer
     * When there is a server error
     * Then I receive an on error event through the observable.</p>
     */
    @Test
    public void testObserverNotifiesServerErrors()
    {
        try {
            doNothing().when(mLightstreamerClient).connect();

            TestSubscriber<ClientStatus> testSubscriber = new TestSubscriber<>();
            mRxLightStreamerClient.connect("http://localhost:8080", "Demo");
            mRxLightStreamerClient.getClientStatusObservable().subscribe(testSubscriber);
            mLightstreamerClient.getListeners().get(0).onStatusChange(ClientStatus.CONNECTING.getLightStreamerStatus());
            mLightstreamerClient.getListeners().get(0).onStatusChange(ClientStatus.STREAM_SENSING.getLightStreamerStatus());
            mLightstreamerClient.getListeners().get(0).onServerError(61, "Whatever");

            List<ClientStatus> updates = testSubscriber.getOnNextEvents();
            List<Throwable> errorEvents = testSubscriber.getOnErrorEvents();
            Throwable errorEvent = errorEvents.get(0);

            assertThat(updates).isNotNull();
            assertThat(updates).isNotEmpty();
            assertThat(updates).hasSize(2);
            assertThat(updates.get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(updates.get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(errorEvents).isNotNull();
            assertThat(errorEvents).isNotEmpty();
            assertThat(errorEvents).hasSize(1);
            assertThat(errorEvent).isInstanceOf(LightStreamerServerError.class);
            assertThat(errorEvent).hasMessage("Whatever");
            assertThat(((LightStreamerServerError) errorEvent).getServerError())
                    .isEqualTo(ServerError.SERVER_RESPONSE_PARSING_ERROR);


            verify(mLightstreamerClient).connect();
            verify(mLightstreamerClient).addListener(any());
            verify(mLightstreamerClient, times(4)).getListeners();
            verifyNoMoreInteractions(mLightstreamerClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscribe.</p>
     * <p>Given I am connected to LS
     * When I subscribe to a subscription
     * Then it is properly subscribed.</p>
     */
    @Test
    public void testClientCanSubscribe()
    {
        try {
            when(mRxSubscription.getLSSubscription()).thenReturn(mLSSubscription);
            mRxLightStreamerClient.subscribe(mRxSubscription);
            assertThat(mRxLightStreamerClient.getSubscriptionsCount()).isEqualTo(1);
            assertThat(mRxLightStreamerClient.getSubscription(0)).isEqualTo(mRxSubscription);
            verify(mRxSubscription).getLSSubscription();
            verify(mLightstreamerClient).subscribe(mLSSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscribe.</p>
     * <p>Given I am connected to LS
     * When I unsubscribe to a subscription
     * Then it is properly unsubscribed.</p>
     */
    @Test
    public void testClientCanUnsubscribe()
    {
        try {
            when(mRxSubscription.getLSSubscription()).thenReturn(mLSSubscription);
            mRxLightStreamerClient.subscribe(mRxSubscription);
            assertThat(mRxLightStreamerClient.getSubscriptionsCount()).isEqualTo(1);
            assertThat(mRxLightStreamerClient.getSubscription(0)).isEqualTo(mRxSubscription);
            mRxLightStreamerClient.unsubscribe(mRxSubscription);
            assertThat(mRxLightStreamerClient.getSubscriptionsCount()).isEqualTo(0);
            verify(mRxSubscription, times(2)).getLSSubscription();
            verify(mLightstreamerClient).subscribe(mLSSubscription);
            verify(mLightstreamerClient).unsubscribe(mLSSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Getting connection options for the given client.</p>
     * <p>Given I am in the application
     * When I need to look at the connection options
     * Then I get the object in the LightStreamer client.</p>
     */
    @Test
    public void testConnectionOptionsReturnsTheConnectionOptionsOfTheLightStreamerClient()
    {
        try {
            assertThat(mRxLightStreamerClient.getConnectionOptions()).isEqualTo(mLightstreamerClient.connectionOptions);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
