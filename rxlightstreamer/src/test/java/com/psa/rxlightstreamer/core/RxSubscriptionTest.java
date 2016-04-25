package com.psa.rxlightstreamer.core;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.Subscription;
import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.exceptions.LightStreamerSubscriptionError;
import com.psa.rxlightstreamer.helpers.SubscriptionError;
import com.psa.rxlightstreamer.injection.DaggerRxLightStreamerComponent;
import com.psa.rxlightstreamer.injection.RxLightStreamerInjector;
import com.psa.rxlightstreamer.injection.TestCoreModule;
import com.psa.rxlightstreamer.test.helpers.TestSubscription;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import rx.observers.TestSubscriber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>Feature: As a user, I want to be able to subscribe to different adapters to get updates from
 * LightStreamer.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxSubscriptionTest extends BaseTest {
    private TestSubscription mStringRxSubscription;
    @Spy
    private Subscription mLSSubscription;
    @Mock
    private ItemUpdate mItemUpdate;
    private static final String[] mFields = {"FIELD"};
    private static final String[] mItems = {"ITEM"};
    private TestSubscriber<RxSubscription.SubscriptionEvent<String>> mSubscriptionEventSubscriber;

    @Override
    public void setUp() {
        mLSSubscription = new Subscription("MERGE");
        super.setUp();
        TestCoreModule testCoreModule = new TestCoreModule();
        testCoreModule.setMergeSubscription(mLSSubscription);
        RxLightStreamerInjector.setRxLightStreamerComponent(DaggerRxLightStreamerComponent.builder()
                .coreModule(testCoreModule)
                .build());
        mSubscriptionEventSubscriber = new TestSubscriber<>();
        mStringRxSubscription = new TestSubscription();
    }

    /**
     * <p>Scenario: Subscription is set properly.</p>
     * <p>Given I am connected to LS
     * When I want to subscribe an adapter
     * Then I get the subscription I set up.</p>
     */
    @Test
    public void testSubscriptionIsSetProperly()
    {
        try {
            assertThat(mLSSubscription.getDataAdapter()).isEqualTo("DEMO");
            assertThat(mLSSubscription.getFields()).isEqualTo(mFields);
            assertThat(mLSSubscription.getItems()).isEqualTo(mItems);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Observable emits event when the subscription is subscribed.</p>
     * <p>Given I am connected to LS
     * When I subscribe
     * Then I get an event confirming it.</p>
     */
    @Test
    public void testSubscriptionEventIsEmittedWhenTheSubscriptionIsSubscribed()
    {
        try {
            mStringRxSubscription.getSubscriptionObservable().subscribe(mSubscriptionEventSubscriber);
            mLSSubscription.getListeners().get(0).onSubscription();
            List<RxSubscription.SubscriptionEvent<String>> subscriptionEvents =
                    mSubscriptionEventSubscriber.getOnNextEvents();
            assertThat(subscriptionEvents).isNotEmpty().hasSize(1);
            assertThat(subscriptionEvents.get(0).isSubscribed()).isTrue();
            assertThat(subscriptionEvents.get(0).getUpdatedItem()).isNull();
            verify(mLSSubscription, times(2)).getListeners();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Observable emits event when the subscription is unsubscribed.</p>
     * <p>Given I am connected to LS
     * When I unsubscribe
     * Then I get an event confirming it.</p>
     */
    @Test
    public void testSubscriptionEventIsEmittedWhenTheSubscriptionIsUnSubscribed()
    {
        try {
            mStringRxSubscription.getSubscriptionObservable().subscribe(mSubscriptionEventSubscriber);
            mLSSubscription.getListeners().get(0).onUnsubscription();
            List<RxSubscription.SubscriptionEvent<String>> subscriptionEvents =
                    mSubscriptionEventSubscriber.getOnNextEvents();
            assertThat(subscriptionEvents).isNotEmpty().hasSize(1);
            assertThat(subscriptionEvents.get(0).isSubscribed()).isFalse();
            assertThat(subscriptionEvents.get(0).getUpdatedItem()).isNull();
            verify(mLSSubscription, times(5)).getListeners();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Observable emits event when an item is updated.</p>
     * <p>Given I am connected to LS
     * When I get an update
     * Then I get an event confirming it.</p>
     */
    @Test
    public void testSubscriptionEventIsEmittedWhenItemIsUpdated()
    {
        try {
            when(mItemUpdate.getValue(0)).thenReturn("Test");
            mStringRxSubscription.getSubscriptionObservable().subscribe(mSubscriptionEventSubscriber);
            mLSSubscription.getListeners().get(0).onItemUpdate(mItemUpdate);
            List<RxSubscription.SubscriptionEvent<String>> subscriptionEvents =
                    mSubscriptionEventSubscriber.getOnNextEvents();
            assertThat(subscriptionEvents).isNotEmpty().hasSize(1);
            assertThat(subscriptionEvents.get(0).isSubscribed()).isTrue();
            assertThat(subscriptionEvents.get(0).getUpdatedItem()).isEqualTo("Test");
            verify(mLSSubscription, times(2)).getListeners();
            verify(mItemUpdate).getValue(0);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Observable emits event when error occurs.</p>
     * <p>Given I am connected to LS
     * When I get an error
     * Then I get an event confirming it.</p>
     */
    @Test
    public void testSubscriptionErrorEventIsEmittedWhenSubscriptionHandleError()
    {
        try {
            mStringRxSubscription.getSubscriptionObservable().subscribe(mSubscriptionEventSubscriber);
            mLSSubscription.getListeners().get(0).onSubscriptionError(17, "Test message.");
            List<Throwable> errorEvents = mSubscriptionEventSubscriber.getOnErrorEvents();
            assertThat(errorEvents).isNotNull().isNotEmpty().hasSize(1);
            Throwable throwable = errorEvents.get(0);
            assertThat(throwable).hasMessage("Test message.");
            assertThat(throwable).isInstanceOf(LightStreamerSubscriptionError.class);
            assertThat(((LightStreamerSubscriptionError) throwable).getSubscriptionError())
                    .isEqualTo(SubscriptionError.BAD_DATA_ADAPTER_NAME);
            verify(mLSSubscription, times(2)).getListeners();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
