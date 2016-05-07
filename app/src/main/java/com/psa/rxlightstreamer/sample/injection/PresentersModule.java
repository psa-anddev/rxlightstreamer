package com.psa.rxlightstreamer.sample.injection;

import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.presenters.StatusBarPresenter;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * <p>This module injects the presenters.</p>
 *
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
@Module
public class PresentersModule
{
    @Inject
    ServiceMediator mServiceMediator;

    @Provides public StatusBarPresenter provideStatusBarPresenter()
    {
        return new StatusBarPresenter(mServiceMediator);
    }
}
