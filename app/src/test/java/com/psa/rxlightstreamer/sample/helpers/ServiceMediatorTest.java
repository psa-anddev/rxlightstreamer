package com.psa.rxlightstreamer.sample.helpers;

import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.sample.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static com.psa.rxlightstreamer.helpers.ClientStatus.CONNECTING;
import static com.psa.rxlightstreamer.helpers.ClientStatus.DISCONNECTED;
import static com.psa.rxlightstreamer.helpers.ClientStatus.STREAM_SENSING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static rx.Observable.just;

/**
 * <p>Feature: As a user, I want to interact with the LightStreamer session.</p>
 *
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class ServiceMediatorTest extends BaseTest
{
    private ServiceMediator mServiceMediator;
    private TestSubscriber<ClientStatus> mClientStatusTestSubscriber;
    private TestSubscriber<ServiceEvent> mServiceEventTestSubscriber;
    @Mock private RxSubscription mUnifiedSubscription;
    @Mock private RxNonUnifiedSubscription mNonUnifiedSubscription;

    @Override
    public void setUp()
    {
        super.setUp();
        mServiceMediator = new ServiceMediator();
        mClientStatusTestSubscriber = new TestSubscriber<>();
        mServiceEventTestSubscriber = new TestSubscriber<>();
    }

    /**
     * <p>Scenario: Mediator repeats client events.</p>
     * <p>Given I am connected to LightStreamer
     *When my connection status changes
     *Then the mediator repeats the event.</p>
     */
    @Test
    public void testClientStatusEventsAreResend()
    {
        try
        {
            mServiceMediator.getClientStatusSubject().subscribe(mClientStatusTestSubscriber);
            Observable<ClientStatus> clientStatusObservable = just(CONNECTING, STREAM_SENSING);
            clientStatusObservable.subscribe(
                    status -> {
                        mServiceMediator.getClientStatusSubject().onNext(status);
                    }
            );
            List<ClientStatus> events = mClientStatusTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(3);
            assertThat(events).contains(DISCONNECTED, CONNECTING, STREAM_SENSING);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
    * <p>Scenario: Connection event is sent to the server upon connection request.</p>
    * <p>Given I am in the application
    * When I want to connect to LightStreamer
    * Then An event is sent to the service.</p>
    */
    @Test
    public void testConnectSendsAConnectionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.connect("ls_host", "ls_adapterSet");
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.CONNECT);
            assertThat(serviceEvent.getLSHost()).isEqualTo("ls_host");
            assertThat(serviceEvent.getAdapterSet()).isEqualTo("ls_adapterSet");
            assertThat(serviceEvent.getSubscription()).isNull();
            assertThat(serviceEvent.getNonUnifiedSubscription()).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Disconnection event is sent to the server upon disconnection request.</p>
     * <p>Given I am in the application
     * When I want to disconnect from LightStreamer
     * Then An event is sent to the service.</p>
     */
    @Test
    public void testDisconnectSendsADisconnectionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.disconnect();
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.DISCONNECT);
            assertThat(serviceEvent.getLSHost()).isNull();
            assertThat(serviceEvent.getAdapterSet()).isNull();
            assertThat(serviceEvent.getSubscription()).isNull();
            assertThat(serviceEvent.getNonUnifiedSubscription()).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscription event is sent to the server upon subscription request.</p>
     * <p>Given I am in the application
     * When I want to subscribe to an adapter in LightStreamer
     * Then An event is sent to the service.</p>
     */
    @Test
    public void testSubscriptionInUnifiedModeSendsASubscriptionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.subscribe(mUnifiedSubscription);
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.SUBSCRIBE);
            assertThat(serviceEvent.getLSHost()).isNull();
            assertThat(serviceEvent.getAdapterSet()).isNull();
            assertThat(serviceEvent.getSubscription()).isEqualTo(mUnifiedSubscription);
            assertThat(serviceEvent.getNonUnifiedSubscription()).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscription event is sent to the server upon unsubscription request.</p>
     * <p>Given I am in the application
     * When I want to unsubscribe to an adapter in LightStreamer
     * Then An event is sent to the service.</p>
     */
    @Test
    public void testUnsubscriptionInUnifiedModeSendsAnUnsubscriptionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.unsubscribe(mUnifiedSubscription);
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.UNSUBSCRIBE);
            assertThat(serviceEvent.getLSHost()).isNull();
            assertThat(serviceEvent.getAdapterSet()).isNull();
            assertThat(serviceEvent.getSubscription()).isEqualTo(mUnifiedSubscription);
            assertThat(serviceEvent.getNonUnifiedSubscription()).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscription event is sent to the server upon subscription request.</p>
     * <p>Given I am in the application
     * When I want to subscribe to an adapter in LightStreamer
     * Then An event is sent to the service.</p>
     */
    @Test
    public void testSubscriptionInNonUnifiedModeSendsASubscriptionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.subscribe(mNonUnifiedSubscription);
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.SUBSCRIBE);
            assertThat(serviceEvent.getLSHost()).isNull();
            assertThat(serviceEvent.getAdapterSet()).isNull();
            assertThat(serviceEvent.getSubscription()).isNull();
            assertThat(serviceEvent.getNonUnifiedSubscription()).isEqualTo(mNonUnifiedSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscription event is sent to the server upon unsubscription request.</p>
     * <p>Given I am in the application
     * When I want to unsubscribe to an adapter in LightStreamer
     * Then An event is sent to the service.</p>
     */
    @Test
    public void testUnsubscriptionInNonUnifiedModeSendsAnUnsubscriptionEventToTheService()
    {
        try
        {
            mServiceMediator.getServiceSubject().subscribe(mServiceEventTestSubscriber);
            mServiceMediator.unsubscribe(mNonUnifiedSubscription);
            List<ServiceEvent> events = mServiceEventTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);
            ServiceEvent serviceEvent = events.get(0);
            assertThat(serviceEvent.getEventType()).isEqualTo(ServiceEvent.EventType.UNSUBSCRIBE);
            assertThat(serviceEvent.getLSHost()).isNull();
            assertThat(serviceEvent.getAdapterSet()).isNull();
            assertThat(serviceEvent.getSubscription()).isNull();
            assertThat(serviceEvent.getNonUnifiedSubscription()).isEqualTo(mNonUnifiedSubscription);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
