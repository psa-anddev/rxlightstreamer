package com.psa.rxlightstreamer.sample.subscriptions;

import com.lightstreamer.client.ItemUpdate;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.sample.BaseTest;
import com.psa.rxlightstreamer.sample.test.subscriptions.QuoteTestSubscription;

import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * <p>Feature: The quote subscription returns the prices as expected.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class QuoteSubscriptionTest extends BaseTest {
    @Mock
    private ItemUpdate mItemUpdate;

    private QuoteTestSubscription mQuoteTestSubscription;
    private TestSubscriber<RxSubscription.SubscriptionEvent<QuoteSubscription.Quote>> mSubscriptionEventTestSubscriber;

    @Override
    public void setUp() {
        super.setUp();
        mQuoteTestSubscription = new QuoteTestSubscription();
        mSubscriptionEventTestSubscriber = new TestSubscriber<>();
    }

    /**
     * <p>Scenario: Map operator successfully converts the item update into a quote object.</p>
     * <p>Given I am subscribed to a quote subscription
     * When the listener receives a new item update
     * Then I get the quote object associated with it.</p>
     */
    @Test
    public void testSubscriptionIsMappedProperly()
    {
        try {
            when(mItemUpdate.getItemName()).thenReturn("item1");
            when(mItemUpdate.getValue("stock_name")).thenReturn("Test stock");
            when(mItemUpdate.getValue("last_price")).thenReturn("0.05");
            when(mItemUpdate.getValue("time")).thenReturn("17:15:51");
            when(mItemUpdate.getValue("pct_change")).thenReturn("1");
            when(mItemUpdate.getValue("bid_quantity")).thenReturn("2");
            when(mItemUpdate.getValue("bid")).thenReturn("0.025");
            when(mItemUpdate.getValue("ask")).thenReturn("0.75");
            when(mItemUpdate.getValue("ask_quantity")).thenReturn("1");
            when(mItemUpdate.getValue("min")).thenReturn("0.05");
            when(mItemUpdate.getValue("max")).thenReturn("0.75");
            when(mItemUpdate.getValue("ref_price")).thenReturn("0.15");
            when(mItemUpdate.getValue("open_price")).thenReturn("0.16");

            mQuoteTestSubscription.setRawObservable(Observable.just(new RxSubscription.SubscriptionEvent<>(true, mItemUpdate)));
            mQuoteTestSubscription.getSubscriptionObservable().subscribe(mSubscriptionEventTestSubscriber);
            List<RxSubscription.SubscriptionEvent<QuoteSubscription.Quote>> events = mSubscriptionEventTestSubscriber.getOnNextEvents();
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

            verify(mItemUpdate).getItemName();
            verify(mItemUpdate).getValue("stock_name");
            verify(mItemUpdate).getValue("last_price");
            verify(mItemUpdate).getValue("time");
            verify(mItemUpdate).getValue("pct_change");
            verify(mItemUpdate).getValue("bid_quantity");
            verify(mItemUpdate).getValue("bid");
            verify(mItemUpdate).getValue("ask");
            verify(mItemUpdate).getValue("ask_quantity");
            verify(mItemUpdate).getValue("min");
            verify(mItemUpdate).getValue("max");
            verify(mItemUpdate).getValue("ref_price");
            verify(mItemUpdate).getValue("open_price");
            verifyNoMoreInteractions(mItemUpdate);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
