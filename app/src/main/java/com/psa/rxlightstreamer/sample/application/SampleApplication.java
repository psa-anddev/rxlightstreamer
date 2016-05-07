package com.psa.rxlightstreamer.sample.application;

import android.app.Application;

import com.psa.rxlightstreamer.sample.injection.ApplicationComponent;
import com.psa.rxlightstreamer.sample.injection.DaggerApplicationComponent;
import com.psa.rxlightstreamer.sample.subscriptions.QuoteSubscription;

import java.util.List;

/**
 * <p>This is the application context for the sample Application.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class SampleApplication extends Application {
    private ApplicationComponent mApplicationComponent;
    private List<QuoteSubscription.Quote> mQuotes;
    /**
     * <p>Returns the application component.</p>
     * @return application component.
     */
    public ApplicationComponent getApplicationComponent()
    {
        return mApplicationComponent;
    }

    /**
     * <p>Sets the application component for tests.</p>
     * @param applicationComponent application component to use in tests.
     */
    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    /**
     * <p>Return the list of quotes.</p>
     * @return the list of quotes.
     */
    public List<QuoteSubscription.Quote> getQuotes() {
        return mQuotes;
    }

    /**
     * <p>Set the list of quotes.</p>
     * @param quotes list of quotes to set.
     */
    public void setQuotes(List<QuoteSubscription.Quote> quotes) {
        mQuotes = quotes;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().build();
    }
}
