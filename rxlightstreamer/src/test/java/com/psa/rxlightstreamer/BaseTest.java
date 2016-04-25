package com.psa.rxlightstreamer;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class BaseTest {
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testNothing()
    {

    }
}