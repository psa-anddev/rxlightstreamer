package com.psa.rxlightstreamer.sample.injection;

import com.psa.rxlightstreamer.sample.presenters.StatusBarPresenter;
import com.psa.rxlightstreamer.sample.services.LightStreamerService;
import com.psa.rxlightstreamer.sample.ui.activities.MainActivity;
import com.psa.rxlightstreamer.sample.ui.adapters.StockListAdapter;
import com.psa.rxlightstreamer.sample.ui.fragments.StatusFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * <p>Injects the objects in the application</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
@Component(modules = {LightStreamerModule.class, PresentersModule.class}) @Singleton
public interface ApplicationComponent {
    void inject(LightStreamerService lightStreamerService);
    void inject(MainActivity mainActivity);
    void inject(StatusFragment statusFragment);
    void inject(StockListAdapter stockListAdapter);
    void inject(StatusBarPresenter statusBarPresenter);
}
