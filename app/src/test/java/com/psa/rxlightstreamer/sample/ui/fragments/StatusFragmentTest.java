package com.psa.rxlightstreamer.sample.ui.fragments;

import com.psa.rxlightstreamer.sample.BaseTest;
import com.psa.rxlightstreamer.sample.R;
import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.helpers.ServiceMediator;
import com.psa.rxlightstreamer.sample.injection.DaggerApplicationComponent;
import com.psa.rxlightstreamer.sample.injection.TestLightStreamerModule;
import com.psa.rxlightstreamer.sample.injection.TestPresentersModule;
import com.psa.rxlightstreamer.sample.presenters.StatusBarPresenter;

import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import rx.Observable;

import static junit.framework.Assert.fail;
import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * <p>As a user I want to be able to see my connection status.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class StatusFragmentTest extends BaseTest
{
    private StatusFragment mStatusFragment;
    @Mock private StatusBarPresenter mStatusBarPresenter;
    @Mock private ServiceMediator mServiceMediator;

    @Override
    public void setUp()
    {
        super.setUp();
        mStatusFragment = new StatusFragment();
        TestPresentersModule testPresentersModule = new TestPresentersModule();
        TestLightStreamerModule testLightStreamerModule = new TestLightStreamerModule();
        testLightStreamerModule.setServiceMediator(mServiceMediator);
        testPresentersModule.setStatusBarPresenter(mStatusBarPresenter);
        ((SampleApplication) RuntimeEnvironment.application).setApplicationComponent(
                DaggerApplicationComponent.builder()
                .lightStreamerModule(testLightStreamerModule)
                .presentersModule(testPresentersModule).build()
        );
    }

    /**
     * <p>Scenaio: Fragment shows the status.</p>
     * <p>Gven I am in the main activity
     * When the connection status changes
     * Then I can see the status in the status bar.</p>
     */
    @Test
    public void testTextIsUpdatedUponPresentersRequest()
    {
        try
        {
            when(mStatusBarPresenter.getItemsObservable()).thenReturn(Observable.just(R.string.settings));
            SupportFragmentTestUtil.startFragment(mStatusFragment);
            assertThat(mStatusFragment.mStatusText).hasText(R.string.settings);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
