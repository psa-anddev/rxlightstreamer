package com.psa.rxlightstreamer.exceptions;

import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.helpers.SubscriptionError;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Feature: As a user, I want to have accurate information when a subscription error
 * comes to pass.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class LightStreamerSubscriptionErrorTest extends BaseTest {
    private LightStreamerSubscriptionError mLightStreamerSubscriptionError;
    private SubscriptionError mSubscriptionError;

    @Override
    public void setUp() {
        super.setUp();
        mSubscriptionError = SubscriptionError.BAD_DATA_ADAPTER_NAME;
        mLightStreamerSubscriptionError = new LightStreamerSubscriptionError(mSubscriptionError, "Message");
    }

    /**
     * <p>Scenario: LightStreamer subscription error is instantiated correctly.</p>
     * <p>Given I am connected to LS
     * When I get a subscription error
     * Then the information is accurate.</p>
     */
    @Test
    public void testExceptionInstantiationIsCorrect()
    {
        try {
            assertThat(mLightStreamerSubscriptionError).hasMessage("Message");
            assertThat(mLightStreamerSubscriptionError.getSubscriptionError()).isEqualTo(mSubscriptionError);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
