package com.psa.rxlightstreamer.exceptions;

import com.psa.rxlightstreamer.BaseTest;
import com.psa.rxlightstreamer.helpers.ServerError;

import org.junit.Test;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Feature: As a developer, I want to be able to throw an exception every time I get an error from
 * the LightStreamer client.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class LightStreamerServerErrorTest extends BaseTest {
    @SuppressWarnings("FieldCanBeLocal")
    private LightStreamerServerError mLightStreamerServerError;

    /**
     * <p>Scenario: LightStreamer server error instantiation.</p>
     * <p>Given I am connected to LightStreamer
     * When I get a server error
     * Then I want to have the right information about it.</p>
     */
    @Test
    public void testLightStreamerServerErrorInstantiation()
    {
        try {
            mLightStreamerServerError =
                    new LightStreamerServerError(ServerError.SERVER_RESPONSE_PARSING_ERROR, "Whatever");
            assertThat(mLightStreamerServerError).isNotNull().hasMessage("Whatever");
            assertThat(mLightStreamerServerError.getServerError())
                    .isEqualTo(ServerError.SERVER_RESPONSE_PARSING_ERROR);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
