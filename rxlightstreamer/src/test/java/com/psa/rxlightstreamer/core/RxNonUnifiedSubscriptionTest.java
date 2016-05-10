package com.psa.rxlightstreamer.core;

import com.lightstreamer.ls_client.SubscribedTableKey;
import com.lightstreamer.ls_client.UpdateInfo;
import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.helpers.SubscriptionType;

import org.junit.Test;
import org.mockito.Mock;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * <p>Feature: As a user, I want to be able to subscribe to subscriptions using the non unified
 * API.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class RxNonUnifiedSubscriptionTest extends BaseTest {
    //region Internal classes
    public static class TestNonUnifiedSubscription extends RxNonUnifiedSubscription<UpdateInfo>
    {

        public TestNonUnifiedSubscription() {
            super(SubscriptionType.MERGE, "DEMO", new String[]{"number"}, new String[]{"1", "2"}, false);
        }

        @Override
        public Observable<RxSubscription.SubscriptionEvent<UpdateInfo>> getSubscriptionObservable() {
            return mRawObservable;
        }
    }
    //endregion

    //region Private fields
    private TestSubscriber<RxSubscription.SubscriptionEvent<UpdateInfo>> mTestSubscriber;
    @Mock
    private UpdateInfo mUpdateInfo;
    private TestNonUnifiedSubscription mTestNonUnifiedSubscription;
    //endregion

    //region Tests set up

    @Override
    public void setUp() {
        super.setUp();
        mTestSubscriber = new TestSubscriber<>();
        mTestNonUnifiedSubscription = new TestNonUnifiedSubscription();
    }
    //endregion

    //region Tests

    /**
     * <p>Scenario: Subscription is set properly.</p>
     * <p>Given I am in the application
     * When I instantiate a subscription
     * Then all data is correct.</p>
     */
    @Test
    public void testSubscriptionIsSetCorrectly()
    {
        try {
            mTestNonUnifiedSubscription.getSubscriptionObservable().subscribe(mTestSubscriber);
            assertThat(mTestNonUnifiedSubscription.getSubscriptionType()).isEqualTo(SubscriptionType.MERGE);
            assertThat(mTestNonUnifiedSubscription.getAdapter()).isEqualTo("DEMO");
            assertThat(mTestNonUnifiedSubscription.getFields()).isEqualTo(new String[]{"number"});
            assertThat(mTestNonUnifiedSubscription.getItems()).isEqualTo(new String[]{"1", "2"});
            assertThat(mTestNonUnifiedSubscription.isSnapshot()).isFalse();
            assertThat(mTestNonUnifiedSubscription.getExtendedTableInfo().getFields()).isEqualTo(mTestNonUnifiedSubscription.getFields());
            assertThat(mTestNonUnifiedSubscription.getExtendedTableInfo().getItems()).isEqualTo(mTestNonUnifiedSubscription.getItems());
            assertThat(mTestNonUnifiedSubscription.getExtendedTableInfo().getDataAdapter()).isEqualTo(mTestNonUnifiedSubscription.getAdapter());
            assertThat(mTestNonUnifiedSubscription.getExtendedTableInfo().getMode()).isEqualTo(mTestNonUnifiedSubscription.getSubscriptionType().getLSSubscriptionType());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscription events are emitted</p>
     * <p>Given I am subscribed to an adapter
     * When there is an update
     * Then an event is emitted.</p>
     */
    @Test
    public void testUpdateEventsAreEmitted()
    {
        try {
            mTestNonUnifiedSubscription.getSubscriptionObservable().subscribe(mTestSubscriber);
            mTestNonUnifiedSubscription.setSubscribedTableKey(mock(SubscribedTableKey.class));
            mTestNonUnifiedSubscription.getHandyTableListener().onUpdate(0, "1", mUpdateInfo);
            assertThat(mTestSubscriber.getOnNextEvents().get(1).isSubscribed()).isTrue();
            assertThat(mTestSubscriber.getOnNextEvents().get(1).getUpdatedItem()).isEqualTo(mUpdateInfo);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
    /**
     * <p>Scenario: Unsubscription is notified.</p>
     * <p>Given I am subscribed to an adapter
     * When the adapter is unsubscribed
     * Then an event is emitted.</p>
     */
    @Test
    public void testUnsubscriptionIsNotified()
    {
        try {
            mTestNonUnifiedSubscription.getSubscriptionObservable().subscribe(mTestSubscriber);
            mTestNonUnifiedSubscription.setSubscribedTableKey(mock(SubscribedTableKey.class));
            mTestNonUnifiedSubscription.getHandyTableListener().onUnsubscrAll();
            assertThat(mTestSubscriber.getOnNextEvents().get(1).isSubscribed()).isFalse();
            assertThat(mTestSubscriber.getOnNextEvents().get(1).getUpdatedItem()).isNull();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
    //endregion
}
