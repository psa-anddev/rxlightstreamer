package com.psa.rxlightstreamer.helpers;

import com.psa.rxlightstreamer.BaseTest;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Feature: As a user I want to have accurate information about subscription errors.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class SubscriptionErrorTest extends BaseTest {
    /**
     * <p>Scenario: LS codes conversion to subscription error objects.</p>
     * <p>Given I am connected to LS
     * When I get a subscription error
     * Then I get an accurate representation.</p>
     */
    @Test
    public void testConversionIsCorrect()
    {
        try {
            assertThat(SubscriptionError.fromLSCode(17)).isEqualTo(SubscriptionError.BAD_DATA_ADAPTER_NAME);
            assertThat(SubscriptionError.fromLSCode(20)).isEqualTo(SubscriptionError.SESSION_INTERRUPTED);
            assertThat(SubscriptionError.fromLSCode(21)).isEqualTo(SubscriptionError.BAD_GROUP_NAME);
            assertThat(SubscriptionError.fromLSCode(22)).isEqualTo(SubscriptionError.BAD_GROUP_NAME_FOR_SCHEMA);
            assertThat(SubscriptionError.fromLSCode(23)).isEqualTo(SubscriptionError.BAD_SCHEMA_NAME);
            assertThat(SubscriptionError.fromLSCode(24)).isEqualTo(SubscriptionError.MODE_NOT_ALLOWED_FOR_ITEM);
            assertThat(SubscriptionError.fromLSCode(25)).isEqualTo(SubscriptionError.BAD_SELECTOR_NAME);
            assertThat(SubscriptionError.fromLSCode(26)).isEqualTo(SubscriptionError.UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_FREQ_LIMIT);
            assertThat(SubscriptionError.fromLSCode(27)).isEqualTo(SubscriptionError.UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_FREQ_PREFILTERING);
            assertThat(SubscriptionError.fromLSCode(28)).isEqualTo(SubscriptionError.UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_LICENCE_TERMS);
            assertThat(SubscriptionError.fromLSCode(29)).isEqualTo(SubscriptionError.RAW_MODE_NOT_ALLOWED);
            assertThat(SubscriptionError.fromLSCode(30)).isEqualTo(SubscriptionError.SUBSCRIPTION_NOT_ALLOWED);
            SubscriptionError subscriptionError = SubscriptionError.fromLSCode(-2);
            assertThat(subscriptionError).isEqualTo(SubscriptionError.SUBSCRIPTION_OR_UNSUBSCRIPTION_REQUEST_REJECTED);
            assertThat(subscriptionError.getCode()).isEqualTo(-2);
            assertThat(SubscriptionError.fromLSCode(10)).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
