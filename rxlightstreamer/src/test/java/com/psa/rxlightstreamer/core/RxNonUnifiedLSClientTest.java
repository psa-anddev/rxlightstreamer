package com.psa.rxlightstreamer.core;

import com.lightstreamer.ls_client.ConnectionInfo;
import com.lightstreamer.ls_client.ConnectionListener;
import com.lightstreamer.ls_client.ExtendedTableInfo;
import com.lightstreamer.ls_client.HandyTableListener;
import com.lightstreamer.ls_client.LSClient;
import com.lightstreamer.ls_client.SubscribedTableKey;
import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.injection.DaggerRxLightStreamerComponent;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;
import com.psa.rxlightstreamer.injection.TestCoreModule;

import org.junit.Test;
import org.mockito.Mock;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Feature: As a user, I can connect using the non unified version of the LightStreamer client.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxNonUnifiedLSClientTest extends BaseTest {
    //region Private fields
    private RxNonUnifiedLSClient mRxNonUnifiedLSClient;
    @Mock
    private LSClient mLSClient;
    @Mock
    private ConnectionInfo mConnectionInfo;
    @Mock
    private RxNonUnifiedSubscription mSubscription;
    @Mock
    private ExtendedTableInfo mExtendedTableInfo;
    @Mock
    private HandyTableListener mHandyTableListener;
    @Mock
    private SubscribedTableKey mSubscribedTableKey;

    private TestSubscriber<ClientStatus> mTestSubscriber;
    //endregion

    //region Tests set up

    @Override
    public void setUp() {
        super.setUp();
        TestCoreModule testCoreModule = new TestCoreModule();
        testCoreModule.setLSClient(mLSClient);
        testCoreModule.setConnectionInfo(mConnectionInfo);
        RxLightStreamerInjector.setRxLightStreamerComponent(
                DaggerRxLightStreamerComponent.builder().coreModule(testCoreModule)
                .build());
        mRxNonUnifiedLSClient = new RxNonUnifiedLSClient();
        mTestSubscriber = new TestSubscriber<>();
    }

    //endregion

    //region Tests

    /**
     * <p>Scenario: Connect with authentication.</p>
     * <p>Given I have a user and a password to connect to LS
     * When I connect the RxClient
     * Then the underlying LSClient is connected.</p>
     */
    @Test
    public void testLSClientConnectsToLightStreamerWhenRxClientIsConnectedWithAuthentication()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Connect without authentication.</p>
     * <p>Given I don't need a user and a password to connect to LS
     * When I connect the RxClient
     * Then the underlying LSClient is connected.</p>
     */
    @Test
    public void testLSClientConnectsToLightStreamerWhenRxClientIsConnectedWithoutAuthentication()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: LightStreamer disconnection</p>
     * <p>Given I am connected to LightStreamer</p>
     * When I want to disconnect
     * Then I get disconnected.
     */
    @Test
    public void testLSClientDisconnectsFromLightStreamerWhenRxClientIsDisconnected()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapter");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.disconnect();
            mRxNonUnifiedLSClient.getListener().onClose();
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.DISCONNECTED);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verify(mLSClient).closeConnection();
            verifyNoMoreInteractions(mLSClient);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Status changes to stream sensing when connection is established.</p>
     * <p>Given I am connecting to LightStreamer
     * When the connection is established
     * Then the status changes to stream sensing.</p>
     */
    @Test
    public void testStatusChangesToStreamSensingWhenConnectionIsEstablished()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Polling connection is established.</p>
     * <p>Given I am connecting to LightStreamer
     * When the server decides to start a polling connection
     * Then the status changes to HTTP polling.</p>
     */
    @Test
    public void testStatusChangesToHttpPollingWhenPollingConnectionIsEstablished()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            mRxNonUnifiedLSClient.getListener().onSessionStarted(true);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.HTTP_POLLING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(2)).isEqualTo(ClientStatus.HTTP_POLLING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Streaming connection is established.</p>
     * <p>Given I am connecting to LightStreamer
     * When the server decides to start a streaming connection
     * Then the status changes to HTTP polling.</p>
     */
    @Test
    public void testStatusChangesToHttpStreamWhenStreamingConnectionIsEstablished()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            mRxNonUnifiedLSClient.getListener().onSessionStarted(false);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.HTTP_STREAMING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(2)).isEqualTo(ClientStatus.HTTP_STREAMING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Connection becomes stalled.</p>
     * <p>Given I am connected to LightStreamer
     * When the connection goes stalled
     * Then the status changes to stalled.</p>
     */
    @Test
    public void testStatusChangesToStalledWhenConnectionGoesStalled()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            mRxNonUnifiedLSClient.getListener().onSessionStarted(true);
            mRxNonUnifiedLSClient.getListener().onActivityWarning(true);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.STALLED);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(2)).isEqualTo(ClientStatus.HTTP_POLLING);
            assertThat(mTestSubscriber.getOnNextEvents().get(3)).isEqualTo(ClientStatus.STALLED);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Connection stops being stalled.</p>
     * <p>Given I am connected to LightStreamer
     * When the connection stops being stalled
     * Then the status changes to the previous.</p>
     */
    @Test
    public void testStatusChangesToPreviousOneWhenConnectionStopsBeingStalled()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            mRxNonUnifiedLSClient.getListener().onSessionStarted(true);
            mRxNonUnifiedLSClient.getListener().onActivityWarning(true);
            mRxNonUnifiedLSClient.getListener().onActivityWarning(false);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.HTTP_POLLING);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(2)).isEqualTo(ClientStatus.HTTP_POLLING);
            assertThat(mTestSubscriber.getOnNextEvents().get(3)).isEqualTo(ClientStatus.STALLED);
            assertThat(mTestSubscriber.getOnNextEvents().get(4)).isEqualTo(ClientStatus.HTTP_POLLING);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Server closes the connection.</p>
     * <p>Given I am connecting to LightStreamer
     * When the server decides to close the connection
     * Then the status changes to Will retry.</p>
     */
    @Test
    public void testStatusChangesToWillRetryWhenServerClosesTheConnection()
    {
        try {
            mRxNonUnifiedLSClient.connect("host", "adapterset", "user", "password");
            mRxNonUnifiedLSClient.getClientStatusObservable().subscribe(mTestSubscriber);
            mRxNonUnifiedLSClient.getListener().onConnectionEstablished();
            mRxNonUnifiedLSClient.getListener().onSessionStarted(false);
            mRxNonUnifiedLSClient.getListener().onEnd(1);
            assertThat(mRxNonUnifiedLSClient.getClientStatusObservable()).isNotNull();
            assertThat(mRxNonUnifiedLSClient.getStatus()).isEqualTo(ClientStatus.WILL_RETRY);
            assertThat(mTestSubscriber.getOnNextEvents().get(0)).isEqualTo(ClientStatus.CONNECTING);
            assertThat(mTestSubscriber.getOnNextEvents().get(1)).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(mTestSubscriber.getOnNextEvents().get(2)).isEqualTo(ClientStatus.HTTP_STREAMING);
            assertThat(mTestSubscriber.getOnNextEvents().get(3)).isEqualTo(ClientStatus.WILL_RETRY);
            verify(mLSClient).openConnection(eq(mConnectionInfo), isA(ConnectionListener.class));
            verifyNoMoreInteractions(mLSClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscribe adapters.</p>
     * <p>Given I am connected to Light Streamer
     * When I want to subscribe to an adapter
     * Then the adapter is subscribed in the underlying client.</p>
     */
    @Test
    public void testClientCanSubscribe()
    {
        try {
            when(mSubscription.getExtendedTableInfo()).thenReturn(mExtendedTableInfo);
            when(mSubscription.getHandyTableListener()).thenReturn(mHandyTableListener);
            when(mLSClient.subscribeTable(mExtendedTableInfo, mHandyTableListener, false)).thenReturn(mSubscribedTableKey);
            mRxNonUnifiedLSClient.subscribe(mSubscription);
            verify(mSubscription).getExtendedTableInfo();
            verify(mSubscription).getHandyTableListener();
            verify(mSubscription).setSubscribedTableKey(mSubscribedTableKey);
            verify(mLSClient).subscribeTable(mExtendedTableInfo, mHandyTableListener, false);
            verifyNoMoreInteractions(mLSClient, mSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscribe adapters.</p>
     * <p>Given I am connected to Light Streamer
     * When I want to unsubscribe to an adapter
     * Then the adapter is unsubscribed in the underlying client.</p>
     */
    @Test
    public void testClientCanUnsubscribe()
    {
        try {
            when(mSubscription.getSubscribedTableKey()).thenReturn(mSubscribedTableKey);
            mRxNonUnifiedLSClient.unsubscribe(mSubscription);
            verify(mSubscription).getSubscribedTableKey();
            verify(mLSClient).unsubscribeTable(mSubscribedTableKey);
            verifyNoMoreInteractions(mLSClient, mSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
    //endregion
}
