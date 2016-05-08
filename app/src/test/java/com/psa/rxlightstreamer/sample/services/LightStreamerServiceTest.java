package com.psa.rxlightstreamer.sample.services;

import android.content.SharedPreferences;

import com.psa.rxlightstreamer.core.RxLightStreamerClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedLSClient;
import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.sample.BaseTest;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceEvent;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.injection.DaggerApplicationComponent;
import com.psa.rxlightstreamer.sample.injection.TestLightStreamerModule;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowPreferenceManager;
import org.robolectric.util.ServiceController;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>As a user, I want to be able to run LightStreamer in the background and interact with it
 * with a mediator.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class LightStreamerServiceTest extends BaseTest
{
    private ServiceController<LightStreamerService> mLightStreamerServiceServiceController;
    private BehaviorSubject<ServiceEvent> mServiceEventSubject;

    private SharedPreferences mSharedPreferences;
    @Mock
    private ServiceMediator mServiceMediator;
    @Mock
    private ServiceEvent mServiceEvent;
    @Mock
    private RxSubscription mUnifiedSubscription;
    @Mock
    private RxNonUnifiedSubscription mNonUnifiedSubscription;
    @Mock
    private RxNonUnifiedLSClient mNonUnifiedClient;
    @Mock
    private RxLightStreamerClient mUnifiedClient;

    @Override
    public void setUp()
    {
        super.setUp();
        mSharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
        TestLightStreamerModule testLightStreamerModule = new TestLightStreamerModule();
        testLightStreamerModule.setServiceMediator(mServiceMediator);
        testLightStreamerModule.setRxLightStreamerClient(mUnifiedClient);
        testLightStreamerModule.setNonUnifiedLSClient(mNonUnifiedClient);
        ((SampleApplication) RuntimeEnvironment.application).setApplicationComponent(
                DaggerApplicationComponent.builder()
                        .lightStreamerModule(testLightStreamerModule)
                        .build()
        );
        mLightStreamerServiceServiceController = Robolectric.buildService(LightStreamerService.class);
        mServiceEventSubject = BehaviorSubject.create();
    }

    /**
     * <p>Scenario: Connecting to LightStreamer client when unified mode is disabled.</p>
     * <p>Given Unified mode is disabled
     * When I connect to LightStreamer
     * Then the non unified client is connected</p>
     */
    @Test
    public void testServiceConnectsToNonUnifiedClientWhenUnifiedModeIsDisabled()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", false).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(mServiceEventSubject);
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create());
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.CONNECT);
            when(mServiceEvent.getLSHost()).thenReturn("ls_host");
            when(mServiceEvent.getAdapterSet()).thenReturn("ls_adapterset");
            when(mNonUnifiedClient.getClientStatusObservable()).thenReturn(Observable.just(ClientStatus.DISCONNECTED));

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);
            mServiceEventSubject.onNext(mServiceEvent);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceMediator).getClientStatusSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getLSHost();
            verify(mServiceEvent).getAdapterSet();
            verify(mNonUnifiedClient).connect("ls_host", "ls_adapterset");
            verify(mNonUnifiedClient).getClientStatusObservable();
            verifyNoMoreInteractions(mServiceEvent, mServiceMediator, mNonUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Connecting to LightStreamer client when unified mode is enabled.</p>
     * <p>Given Unified mode is enabled
     * When I connect to LightStreamer
     * Then the unified client is connected</p>
     */
    @Test
    public void testServiceConnectsToUnifiedClientWhenUnifiedModeIsEnabled()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", true).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.CONNECT);
            when(mServiceEvent.getLSHost()).thenReturn("ls_host");
            when(mServiceEvent.getAdapterSet()).thenReturn("ls_adapterset");
            when(mUnifiedClient.getClientStatusObservable()).thenReturn(Observable.just(ClientStatus.DISCONNECTED));
            when(mServiceMediator.getClientStatusSubject()).thenReturn(BehaviorSubject.create());

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceMediator).getClientStatusSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getLSHost();
            verify(mServiceEvent).getAdapterSet();
            verify(mUnifiedClient).connect("ls_host", "ls_adapterset");
            verify(mUnifiedClient).getClientStatusObservable();
            verifyNoMoreInteractions(mServiceEvent, mServiceMediator, mUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Disconnecting from LightStreamer client when unified mode is disabled.</p>
     * <p>Given Unified mode is disabled
     * When I disconnect from LightStreamer
     * Then the non unified client is disconnected</p>
     */
    @Test
    public void testServiceDisconnectsFromNonUnifiedClientWhenUnifiedModeIsDisabled()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", false).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.DISCONNECT);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mNonUnifiedClient).disconnect();
            verifyNoMoreInteractions(mServiceEvent, mNonUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Disconnecting from LightStreamer client when unified mode is enabled.</p>
     * <p>Given Unified mode is enabled
     * When I disconnect from LightStreamer
     * Then the unified client is disconnected</p>
     */
    @Test
    public void testServiceDisconnectsFromUnifiedClientWhenUnifiedModeIsEnabled()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", true).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.DISCONNECT);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mUnifiedClient).disconnect();
            verifyNoMoreInteractions(mServiceEvent, mUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscribing to non unified adapters.</p>
     * <p>Given Unified mode is disabled
     * And I am connected to LightStreamer
     * When I want to subscribe an adapter
     * Then the non unified adapter is subscribed.</p>
     */
    @Test
    public void testServiceSubscribesToNonUnifiedAdapter()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", false).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.SUBSCRIBE);
            when(mServiceEvent.getNonUnifiedSubscription()).thenReturn(mNonUnifiedSubscription);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getNonUnifiedSubscription();
            verify(mNonUnifiedClient).subscribe(mNonUnifiedSubscription);
            verifyNoMoreInteractions(mServiceEvent, mServiceMediator, mNonUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscribing adapters when unified mode is enabled.</p>
     * <p>Given Unified mode is enabled
     * And I am connected to LightStreamer
     * When I subscribe an adapter
     * Then the adapter is subscribed.</p>
     */
    @Test
    public void testServiceSubscribesUnifiedAdapters()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", true).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.SUBSCRIBE);
            when(mServiceEvent.getSubscription()).thenReturn(mUnifiedSubscription);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getSubscription();
            verify(mUnifiedClient).subscribe(mUnifiedSubscription);
            verifyNoMoreInteractions(mServiceEvent, mUnifiedSubscription, mUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscribing to non unified adapters.</p>
     * <p>Given Unified mode is disabled
     * And I am connected to LightStreamer
     * When I want to unsubscribe an adapter
     * Then the non unified adapter is unsubscribed.</p>
     */
    @Test
    public void testServiceUnsubscribesToNonUnifiedAdapter()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", false).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.UNSUBSCRIBE);
            when(mServiceEvent.getNonUnifiedSubscription()).thenReturn(mNonUnifiedSubscription);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getNonUnifiedSubscription();
            verify(mNonUnifiedClient).unsubscribe(mNonUnifiedSubscription);
            verifyNoMoreInteractions(mServiceEvent, mNonUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Unsubscribing adapters when unified mode is enabled.</p>
     * <p>Given Unified mode is enabled
     * And I am connected to LightStreamer
     * When I unsubscribe an adapter
     * Then the adapter is unsubscribed.</p>
     */
    @Test
    public void testServiceUnsubscribesUnifiedAdapters()
    {
        try
        {
            mSharedPreferences.edit().putBoolean("unified", true).apply();
            when(mServiceMediator.getServiceSubject()).thenReturn(BehaviorSubject.create(mServiceEvent));
            when(mServiceEvent.getEventType()).thenReturn(ServiceEvent.EventType.UNSUBSCRIBE);
            when(mServiceEvent.getSubscription()).thenReturn(mUnifiedSubscription);

            mLightStreamerServiceServiceController.create().attach().startCommand(0, 0);

            verify(mServiceMediator).getServiceSubject();
            verify(mServiceEvent).getEventType();
            verify(mServiceEvent).getSubscription();
            verify(mUnifiedClient).unsubscribe(mUnifiedSubscription);
            verifyNoMoreInteractions(mServiceEvent, mUnifiedClient);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
