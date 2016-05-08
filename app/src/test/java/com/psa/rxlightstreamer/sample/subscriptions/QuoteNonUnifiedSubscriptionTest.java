package com.psa.rxlightstreamer.sample.subscriptions;

import com.lightstreamer.ls_client.UpdateInfo;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.core.RxSubscription.SubscriptionEvent;
import com.psa.rxlightstreamer.sample.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * <p>Feature: As a user, I want to receive stock updates using the non unified API.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class QuoteNonUnifiedSubscriptionTest extends BaseTest
{
    //region Internal classes

    /**
     * <p>This class is created so that the raw observable can be mocked.</p>
     */
    public static class QuoteNonUnifiedTestSubscription extends QuoteNonUnifiedSubscription
    {
        /**
         * <p>Allows the mocking of the raw observable.</p>
         *
         * @param rawObservable is the mock observable to use.
         */
        public void setRawObservable(Observable<SubscriptionEvent<UpdateInfo>> rawObservable)
        {
            mRawObservable = rawObservable;
        }
    }
    //endregion
    
    //region Private fields
    private TestSubscriber<SubscriptionEvent<QuoteSubscription.Quote>> mTestSubscriber;
    private QuoteNonUnifiedTestSubscription mQuoteSubscription;
    @Mock
    private UpdateInfo mRawQuote;
    //endregion
    
    //region Tests set up
    
    @Override
    public void setUp()
    {
        super.setUp();
        mQuoteSubscription = new QuoteNonUnifiedTestSubscription();
        mTestSubscriber = new TestSubscriber<>();
    }
    
    //endregion
    
    //region Tests

    /**
     * <p>Scenario: Subscription status events are received correctly.</p>
     * <p>Given I am connected to LightStreamer
     * When I subscribe to quotes
     * Then I get an event letting the application know.</p>
     */
    @Test
    public void testSubscriptionUpdatesDoNotCrashTheApplication()
    {
        try
        {
            mQuoteSubscription.setRawObservable(just(new SubscriptionEvent<>(true, null)));
            mQuoteSubscription.getSubscriptionObservable().subscribe(mTestSubscriber);
            assertThat(mTestSubscriber.getOnNextEvents()).hasSize(1);
            SubscriptionEvent<QuoteSubscription.Quote> event = mTestSubscriber.getOnNextEvents().get(0);
            assertThat(event.isSubscribed()).isTrue();
            assertThat(event.getUpdatedItem()).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Subscription is mapped correctly.</p>
     * <p>Given I am subscribed to the quote subscription
     * When A new update comes
     * Then it is properly mapped.</p>
     */
    @Test
    public void testSubscriptionIsMappedCorrectly()
    {
        try
        {
            when(mRawQuote.getItemName()).thenReturn("item1");

            when(mRawQuote.isValueChanged("stock_name")).thenReturn(false);
            when(mRawQuote.isValueChanged("last_price")).thenReturn(true);
            when(mRawQuote.isValueChanged("time")).thenReturn(true);
            when(mRawQuote.isValueChanged("pct_change")).thenReturn(true);
            when(mRawQuote.isValueChanged("bid_quantity")).thenReturn(true);
            when(mRawQuote.isValueChanged("bid")).thenReturn(true);
            when(mRawQuote.isValueChanged("ask")).thenReturn(true);
            when(mRawQuote.isValueChanged("ask_quantity")).thenReturn(true);
            when(mRawQuote.isValueChanged("min")).thenReturn(true);
            when(mRawQuote.isValueChanged("max")).thenReturn(true);
            when(mRawQuote.isValueChanged("ref_price")).thenReturn(true);
            when(mRawQuote.isValueChanged("open_price")).thenReturn(true);

            when(mRawQuote.getOldValue("stock_name")).thenReturn("Test stock");
            when(mRawQuote.getNewValue("last_price")).thenReturn("0.05");
            when(mRawQuote.getNewValue("time")).thenReturn("17:15:51");
            when(mRawQuote.getNewValue("pct_change")).thenReturn("1");
            when(mRawQuote.getNewValue("bid_quantity")).thenReturn("2");
            when(mRawQuote.getNewValue("bid")).thenReturn("0.025");
            when(mRawQuote.getNewValue("ask")).thenReturn("0.75");
            when(mRawQuote.getNewValue("ask_quantity")).thenReturn("1");
            when(mRawQuote.getNewValue("min")).thenReturn("0.05");
            when(mRawQuote.getNewValue("max")).thenReturn("0.75");
            when(mRawQuote.getNewValue("ref_price")).thenReturn("0.15");
            when(mRawQuote.getNewValue("open_price")).thenReturn("0.16");

            mQuoteSubscription.setRawObservable(Observable.just(new SubscriptionEvent<>(true, mRawQuote)));
            mQuoteSubscription.getSubscriptionObservable().subscribe(mTestSubscriber);
            List<SubscriptionEvent<QuoteSubscription.Quote>> events = mTestSubscriber.getOnNextEvents();
            assertThat(events).hasSize(1);

            RxSubscription.SubscriptionEvent<QuoteSubscription.Quote> event = events.get(0);
            assertThat(event.isSubscribed()).isTrue();
            QuoteSubscription.Quote quote = event.getUpdatedItem();
            assertThat(quote.getId()).isEqualTo("item1");
            assertThat(quote.getStockName()).isEqualTo("Test stock");
            assertThat(quote.getLastPrice()).isEqualTo(BigDecimal.valueOf(0.05));
            assertThat(quote.getTime()).isEqualTo("17:15:51");
            assertThat(quote.getChange()).isEqualTo(BigDecimal.valueOf(1));
            assertThat(quote.getBidSize()).isEqualTo(2);
            assertThat(quote.getBid()).isEqualTo(BigDecimal.valueOf(0.025));
            assertThat(quote.getAsk()).isEqualTo(BigDecimal.valueOf(0.75));
            assertThat(quote.getAskSize()).isEqualTo(1);
            assertThat(quote.getMin()).isEqualTo(BigDecimal.valueOf(0.05));
            assertThat(quote.getMax()).isEqualTo(BigDecimal.valueOf(0.75));
            assertThat(quote.getRef()).isEqualTo(BigDecimal.valueOf(0.15));
            assertThat(quote.getOpen()).isEqualTo(BigDecimal.valueOf(0.16));

            verify(mRawQuote).getItemName();

            verify(mRawQuote).isValueChanged("stock_name");
            verify(mRawQuote).isValueChanged("last_price");
            verify(mRawQuote).isValueChanged("time");
            verify(mRawQuote).isValueChanged("pct_change");
            verify(mRawQuote).isValueChanged("bid_quantity");
            verify(mRawQuote).isValueChanged("bid");
            verify(mRawQuote).isValueChanged("ask");
            verify(mRawQuote).isValueChanged("ask_quantity");
            verify(mRawQuote).isValueChanged("min");
            verify(mRawQuote).isValueChanged("max");
            verify(mRawQuote).isValueChanged("ref_price");
            verify(mRawQuote).isValueChanged("open_price");

            verify(mRawQuote).getOldValue("stock_name");
            verify(mRawQuote).getNewValue("last_price");
            verify(mRawQuote).getNewValue("time");
            verify(mRawQuote).getNewValue("pct_change");
            verify(mRawQuote).getNewValue("bid_quantity");
            verify(mRawQuote).getNewValue("bid");
            verify(mRawQuote).getNewValue("ask");
            verify(mRawQuote).getNewValue("ask_quantity");
            verify(mRawQuote).getNewValue("min");
            verify(mRawQuote).getNewValue("max");
            verify(mRawQuote).getNewValue("ref_price");
            verify(mRawQuote).getNewValue("open_price");
            verifyNoMoreInteractions(mRawQuote);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
    
    //endregion
}
