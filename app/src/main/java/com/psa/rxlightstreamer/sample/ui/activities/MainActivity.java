package com.psa.rxlightstreamer.sample.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.psa.rxlightstreamer.helpers.ClientStatus;
import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.services.LightStreamerService;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;
import com.psa.rxlightstreamer.sample.ui.adapters.StockListAdapter;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    ServiceMediator mServiceMediator;
    @Inject
    QuoteSubscription mQuoteSubscription;

    @Bind(R.id.stock_list)
    RecyclerView mStockList;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private boolean mServiceConnected = false;
    private boolean mSubscriptionSubscribed = false;
    private List<QuoteSubscription.Quote> mQuotes;

    private Subscription mRxClientSubscription, mRxQuoteSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        ((SampleApplication) getApplication()).getApplicationComponent().inject(this);
        if (savedInstanceState == null)
            startService(new Intent(this, LightStreamerService.class));
        else
            getBundleData(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getBundleData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQuotes = ((SampleApplication) getApplication()).getQuotes();
        if (mQuotes == null)
        {
            mQuotes = new ArrayList<>(10);
            mQuotes.add(new QuoteSubscription.Quote("item1"));
            mQuotes.add(new QuoteSubscription.Quote("item2"));
            mQuotes.add(new QuoteSubscription.Quote("item3"));
            mQuotes.add(new QuoteSubscription.Quote("item4"));
            mQuotes.add(new QuoteSubscription.Quote("item5"));
            mQuotes.add(new QuoteSubscription.Quote("item6"));
            mQuotes.add(new QuoteSubscription.Quote("item7"));
            mQuotes.add(new QuoteSubscription.Quote("item8"));
            mQuotes.add(new QuoteSubscription.Quote("item9"));
            mQuotes.add(new QuoteSubscription.Quote("item10"));
        }
        mStockList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStockList.setAdapter(new StockListAdapter((SampleApplication) getApplication(), mQuotes));
        mRxQuoteSubscription = mQuoteSubscription.getSubscriptionObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.getUpdatedItem() == null) {
                        mSubscriptionSubscribed = s.isSubscribed();
                    }
                    else {
                        QuoteSubscription.Quote updatedQuote = s.getUpdatedItem();
                        for(QuoteSubscription.Quote quote: mQuotes)
                        {
                            if (updatedQuote.getId().equals(quote.getId()))
                            {
                                quote.setStockName(updatedQuote.getStockName());
                                quote.setLastPrice(updatedQuote.getLastPrice());
                                quote.setTime(updatedQuote.getTime());
                                quote.setChange(updatedQuote.getChange());
                                quote.setBid(updatedQuote.getBid());
                                quote.setBidSize(updatedQuote.getBidSize());
                                quote.setAsk(updatedQuote.getAsk());
                                quote.setAskSize(updatedQuote.getAskSize());
                                quote.setOpen(updatedQuote.getOpen());
                                quote.setRef(updatedQuote.getRef());
                                quote.setMax(updatedQuote.getMax());
                                quote.setMin(updatedQuote.getMin());
                            }
                        }
                    }
                }, Throwable::printStackTrace);
        mRxClientSubscription = mServiceMediator.getClientStatusSubject()
                .subscribe(s -> {
                    if (!mSubscriptionSubscribed && (s == ClientStatus.WS_STREAMING || s == ClientStatus.HTTP_STREAMING)) {
                        mServiceMediator.subscribe(mQuoteSubscription);
                        mSubscriptionSubscribed = true;
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("connected", mServiceConnected);
        outState.putBoolean("subscribed", mSubscriptionSubscribed);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRxQuoteSubscription.unsubscribe();
        mRxClientSubscription.unsubscribe();
        ((SampleApplication) getApplication()).setQuotes(mQuotes);
    }

    @OnClick(R.id.connect_button)
    public void onConnectClick()
    {
        if (mServiceConnected) {
            mServiceMediator.unsubscribe(mQuoteSubscription);
            mServiceMediator.disconnect();
            mServiceConnected = false;
            mSubscriptionSubscribed = false;
            mStockList.setAdapter(new StockListAdapter((SampleApplication) getApplication(), mQuotes));
        }
        else
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            //mServiceMediator.connect("http://192.168.0.128:8081", "DEMO");
            mServiceMediator.connect(
                    sharedPreferences.getString("host", "http://localhost:8080"),
                    sharedPreferences.getString("adapterset", "DEMO")
                    );
            mServiceConnected = true;
        }
    }

    /**
     * <p>Recovers everything in the saved instance.</p>
     * @param savedInstanceState saved instance.
     */
    private void getBundleData(Bundle savedInstanceState) {
        mServiceConnected = savedInstanceState.getBoolean("connected");
        mSubscriptionSubscribed = savedInstanceState.getBoolean("subscribed");
    }
}
