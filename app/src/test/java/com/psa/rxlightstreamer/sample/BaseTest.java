package com.psa.rxlightstreamer.sample;

import android.os.Build;

import com.psa.rxlightstreamer.sample.application.SampleApplication;
import com.psa.rxlightstreamer.sample.injection.DaggerApplicationComponent;
import com.psa.rxlightstreamer.sample.injection.TestLightStreamerModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * <p>This the base test class.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.psa.rxlightstreamer.BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class BaseTest {
    protected TestLightStreamerModule mLightStreamerModule;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mLightStreamerModule = new TestLightStreamerModule();
        ((SampleApplication) RuntimeEnvironment.application)
                .setApplicationComponent(
                        DaggerApplicationComponent.builder()
                        .lightStreamerModule(mLightStreamerModule)
                        .build()
                );
    }
    @Test
    public void testNothing() {

    }
}