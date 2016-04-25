package com.psa.rxlightstreamer.helpers;

import com.psa.rxlightstreamer.BaseTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * <p>Feature: As a developer, I can get an easy to handle status representation from
 * a LightStreamer client status.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class ClientStatusTest extends BaseTest {

    /**
     * <p>Scenario: Status can be converted.</p>
     * <p>Given I get the client status
     * When I want to know the LightStreamer value of this status
     * Then I get the correct one.</p>
     */
    @Test
    public void testLightStreamerStatusCanBeGrabbedFromTheClientStatusObject()
    {
        try {
            assertThat(ClientStatus.DISCONNECTED.getLightStreamerStatus()).isEqualTo("DISCONNECTED");
            assertThat(ClientStatus.WILL_RETRY.getLightStreamerStatus()).isEqualTo("DISCONNECTED:WILL-RETRY");
            assertThat(ClientStatus.CONNECTING.getLightStreamerStatus()).isEqualTo("CONNECTING");
            assertThat(ClientStatus.STREAM_SENSING.getLightStreamerStatus()).isEqualTo("CONNECTED:STREAM-SENSING");
            assertThat(ClientStatus.WS_STREAMING.getLightStreamerStatus()).isEqualTo("CONNECTED:WS-STREAMING");
            assertThat(ClientStatus.HTTP_STREAMING.getLightStreamerStatus()).isEqualTo("CONNECTED:HTTP-STREAMING");
            assertThat(ClientStatus.WS_POLLING.getLightStreamerStatus()).isEqualTo("CONNECTED:WS-POLLING");
            assertThat(ClientStatus.HTTP_POLLING.getLightStreamerStatus()).isEqualTo("CONNECTED:HTTP-POLLING");
            assertThat(ClientStatus.STALLED.getLightStreamerStatus()).isEqualTo("STALLED");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }

    /**
     * <p>Scenario: Application Status can be got from its LightStreamer representation.</p>
     * <p>Given I received a LightStreamer status update
     * When I need to know what is the status applicationwise
     * Then I get the right status.</p>
     */
    @Test
    public void testApplicationStatusCanBeGrabbedFromTheLightStreamerOne()
    {
        try {
            assertThat(ClientStatus.fromLSStatus("DISCONNECTED")).isEqualTo(ClientStatus.DISCONNECTED);
            assertThat(ClientStatus.fromLSStatus("DISCONNECTED:WILL-RETRY")).isEqualTo(ClientStatus.WILL_RETRY);
            assertThat(ClientStatus.fromLSStatus("CONNECTING")).isEqualTo(ClientStatus.CONNECTING);
            assertThat(ClientStatus.fromLSStatus("CONNECTED:STREAM-SENSING")).isEqualTo(ClientStatus.STREAM_SENSING);
            assertThat(ClientStatus.fromLSStatus("CONNECTED:WS-STREAMING")).isEqualTo(ClientStatus.WS_STREAMING);
            assertThat(ClientStatus.fromLSStatus("CONNECTED:HTTP-STREAMING")).isEqualTo(ClientStatus.HTTP_STREAMING);
            assertThat(ClientStatus.fromLSStatus("CONNECTED:WS-POLLING")).isEqualTo(ClientStatus.WS_POLLING);
            assertThat(ClientStatus.fromLSStatus("CONNECTED:HTTP-POLLING")).isEqualTo(ClientStatus.HTTP_POLLING);
            assertThat(ClientStatus.fromLSStatus("STALLED")).isEqualTo(ClientStatus.STALLED);
            assertThat(ClientStatus.fromLSStatus("NON-EXISTENT-STATUS")).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
