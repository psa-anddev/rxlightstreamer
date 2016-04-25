package com.psa.rxlightstreamer.helpers;

import com.psa.rxlightstreamer.BaseTest;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Feature: As a developer, I want to be able to convert the subscription types that I use in my
 * application to those used by LightStreamer.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class SubscriptionTypeTest extends BaseTest {
    /**
     * <p>Scenario: Rx subscription types can be converted to LS ones.</p>
     * <p>Given I am connected to LightStreamer
     * When I subscribe to an adapter
     * Then LightStreamer will know the type of subscription I want.</p>
     */
    @Test
    public void testSubscriptionTypesCanReturnItsLSRepresentation()
    {
        try {
            assertThat(SubscriptionType.MERGE.getLSSubscriptionType()).isEqualTo("MERGE");
            assertThat(SubscriptionType.DISTINCT.getLSSubscriptionType()).isEqualTo("DISTINCT");
            assertThat(SubscriptionType.RAW.getLSSubscriptionType()).isEqualTo("RAW");
            assertThat(SubscriptionType.COMMAND.getLSSubscriptionType()).isEqualTo("COMMAND");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
