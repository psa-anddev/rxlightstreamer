package com.psa.rxlightstreamer.sample.ui.adapters;

import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteNonUnifiedSubscription;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>This adapter displays a list of stock values and update them using LightStreamer.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListBaseViewHolder> {
    //region constants
    public static int EMPTY_ITEM_TYPE = 0;
    public static int HEADER_ITEM_TYPE = 1;
    public static int ITEM_TYPE = 2;
    //endregion

    //region Injected objects
    @Inject ServiceMediator mServiceMediator;
    @Inject
    QuoteNonUnifiedSubscription mQuoteNonUnifiedSubscription;
    @Inject QuoteSubscription mQuoteUnifiedSubscription;
    //endregion

    //region properties definition
    private List<QuoteSubscription.Quote> mQuotes;
    private SampleApplication mSampleApplication;
    //endregion

    //region Overridden methods
    @Override
    public StockListBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        StockListBaseViewHolder viewHolder = null;
        if (viewType == ITEM_TYPE)
        {
            view = inflater.inflate(R.layout.item_stocks, parent, false);
            viewHolder = new ItemViewStockListViewHolder(view);
        }
        else if (viewType == HEADER_ITEM_TYPE)
        {
            view = inflater.inflate(R.layout.item_headers, parent, false);
            viewHolder = new HeaderViewStockListViewHolder(view);
        }
        else if (viewType == EMPTY_ITEM_TYPE)
        {
            view = inflater.inflate(R.layout.item_stock_empty, parent, false);
            viewHolder = new EmptyViewStockListViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StockListBaseViewHolder holder, int position) {
        if (holder instanceof ItemViewStockListViewHolder)
            ((ItemViewStockListViewHolder) holder).bind(mQuotes.get(position - 1), mSampleApplication);
    }

    @Override
    public int getItemCount() {
        return mQuotes.size() + 1;
    }

    @Override
    public void onViewDetachedFromWindow(StockListBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof ItemViewStockListViewHolder)
            ((ItemViewStockListViewHolder) holder).unSubscribe();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 1)
            return ITEM_TYPE;
        else if (mQuotes.size() == 0)
            return EMPTY_ITEM_TYPE;
        else
            return HEADER_ITEM_TYPE;
    }

    //endregion

    //region Constructors

    /**
     * <p>Instantiates a new stock list adapter.</p>
     * @param sampleApplication the application context is needed to provide injection.
     * @param quotes is the list of quotes.
     */
    public StockListAdapter(SampleApplication sampleApplication, List<QuoteSubscription.Quote> quotes) {
        sampleApplication.getApplicationComponent().inject(this);
        mQuotes = quotes;
        mSampleApplication = sampleApplication;
    }

    //endregion

    //region Internal classes

    /**
     * <p>This is the base view holder for the adapter.</p>
     */
    public class StockListBaseViewHolder extends RecyclerView.ViewHolder
    {

        public StockListBaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * <p>This view holder holds the empty view.</p>
     */
    public class EmptyViewStockListViewHolder extends StockListBaseViewHolder
    {

        public EmptyViewStockListViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * <p>This view holder holds the header view.</p>
     */
    public class HeaderViewStockListViewHolder extends StockListBaseViewHolder
    {

        public HeaderViewStockListViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * <p>This view holder holds the item view.</p>
     */
    public class ItemViewStockListViewHolder extends StockListBaseViewHolder
    {
        private QuoteSubscription.Quote mQuote;
        private Subscription mSubscription;
        //region View bindings
        @Bind(R.id.stock_name) TextView mStockNameTextView;
        @Bind(R.id.last) TextView mLastPriceTextView;
        @Bind(R.id.change) TextView mChangeTextView;
        @Bind(R.id.time) TextView mTimeTextView;
        //endregion

        /**
         * <p>Instantiates a itme view holder.</p>
         * @param itemView is the item view.
         */
        public ItemViewStockListViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * <p>Binds the data into the view.</p>
         * @param quote is the quote to bind.
         */
        public void bind(QuoteSubscription.Quote quote, SampleApplication sampleApplication)
        {
            boolean subscribe = mQuote == null;
            mQuote = quote;
            if (subscribe)
            {
                Observable<RxSubscription.SubscriptionEvent<QuoteSubscription.Quote>> observable;
                if (PreferenceManager.getDefaultSharedPreferences(sampleApplication).getBoolean("unified", false))
                    observable = mQuoteUnifiedSubscription.getSubscriptionObservable();
                else
                    observable = mQuoteNonUnifiedSubscription.getSubscriptionObservable();
                mSubscription = observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(i -> {
                            QuoteSubscription.Quote quote1 = i.getUpdatedItem();
                            if (quote1 != null && quote1.getId().equals(mQuote.getId()))
                                bind(quote1, sampleApplication);
                        },
                                Throwable::printStackTrace
                        );
            }
            mStockNameTextView.setText(quote.getStockName());
            if (quote.getLastPrice() != null)
                mLastPriceTextView.setText(NumberFormat.getInstance().format(quote.getLastPrice()));
            mTimeTextView.setText(quote.getTime());
            if (quote.getChange() != null)
                mChangeTextView.setText(NumberFormat.getInstance().format(quote.getChange()));
        }

        /**
         * <p>Unsubscribes the subscription observable.</p>
         */
        public void unSubscribe()
        {
            mSubscription.unsubscribe();
        }
    }
    //endregion
}
