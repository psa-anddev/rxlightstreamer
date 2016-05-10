package com.psa.rxlightstreamer.sample.ui.adapters;

import com.psa.rxlightstreamer.sample.BaseTest;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.assertj.android.recyclerview.v7.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * <p>Feature: As a user I want to be able to see the information of the stock markets.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StockListAdapterTest extends BaseTest {
    private StockListAdapter mStockListAdapter;
    @Mock
    private QuoteSubscription.Quote mQuote;
    @Mock
    private QuoteSubscription mQuoteSubscription;
    @Mock
    private StockListAdapter.ItemViewStockListViewHolder mItemViewStockListViewHolder;

    @Override
    public void setUp() {
        super.setUp();
        mLightStreamerModule.setQuoteSubscription(mQuoteSubscription);
    }

    /**
     * <p>Scenario: Empty list adapter.</p>
     * <p>Given I am in the main activity
     * When there are no items in the list
     * Then I can see there is no items.</p>
     */
    @Test
    public void testAdapterGetsAnEmptyViewWhenNoItems() {
        try {
            mStockListAdapter = new StockListAdapter(
                        (SampleApplication) RuntimeEnvironment.application,
                        new ArrayList<>()
                    );
            assertThat(mStockListAdapter).hasItemCount(1);
            assertThat(mStockListAdapter.getItemViewType(0)).isEqualTo(StockListAdapter.EMPTY_ITEM_TYPE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: I can see the stock markets when they are ready.</p>
     * <p>Given I am in the main activity
     * When I have a list of stock markets
     * Then I can see them in there.</p>
     */
    @Test
    public void testAdapterGetsAHeaderViewAndItemViewsWhenTheListIsNotEmpty()
    {
        try {
            List<QuoteSubscription.Quote> quotes = new ArrayList<>(1);
            quotes.add(mQuote);
            mStockListAdapter = new StockListAdapter(
                    (SampleApplication) RuntimeEnvironment.application,
                    quotes
            );
            assertThat(mStockListAdapter).hasItemCount(2);
            assertThat(mStockListAdapter.getItemViewType(0))
                    .isEqualTo(StockListAdapter.HEADER_ITEM_TYPE);
            assertThat(mStockListAdapter.getItemViewType(1))
                    .isEqualTo(StockListAdapter.ITEM_TYPE);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Item view shows the correct data.</p>
     * <p>Given I am in the main activity
     * When I load the adapter
     * Then I see the right items.</p>
     */
    @Test
    public void testDataIsBoundToItemView()
    {
        try {
            List<QuoteSubscription.Quote> quotes = new ArrayList<>();
            quotes.add(mQuote);
            mStockListAdapter = new StockListAdapter((SampleApplication) RuntimeEnvironment.application,
                    quotes);
            mStockListAdapter.bindViewHolder(mItemViewStockListViewHolder, 1);
            verify(mItemViewStockListViewHolder).bind(mQuote, (SampleApplication) RuntimeEnvironment.application);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
