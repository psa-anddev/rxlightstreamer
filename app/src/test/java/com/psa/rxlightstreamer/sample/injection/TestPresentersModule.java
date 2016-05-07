package com.psa.rxlightstreamer.sample.injection;

import com.psa.rxlightstreamer.sample.presenters.StatusBarPresenter;

/**
 * <p>Presenters module for test.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class TestPresentersModule extends PresentersModule
{
    private StatusBarPresenter mStatusBarPresenter;

    @Override
    public StatusBarPresenter provideStatusBarPresenter()
    {
        if (mStatusBarPresenter == null)
            return super.provideStatusBarPresenter();
        else
            return mStatusBarPresenter;
    }

    /**
     * <p>Sets the status bar presenter for testing.</p>
     *
     * @param statusBarPresenter status bar presenter for tests.
     */
    public void setStatusBarPresenter(StatusBarPresenter statusBarPresenter)
    {
        mStatusBarPresenter = statusBarPresenter;
    }
}
