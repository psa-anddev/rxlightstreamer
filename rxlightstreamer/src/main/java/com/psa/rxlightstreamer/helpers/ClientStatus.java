package com.psa.rxlightstreamer.helpers;

/**
 * <p>This enumeration converts the statuses returned by the LightStreamer Client into something that
 * is more easy to read and code with. </p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public enum ClientStatus {
    /**
     * <p>The client is not connected to LightStreamer.</p>
     */
    DISCONNECTED("DISCONNECTED"),
    /**
     * <p>The client was disconnected due to an error and the client will try to reconnect after
     * a timeout.</p>
     */
    WILL_RETRY("DISCONNECTED:WILL-RETRY"),

    /**
     * <p>The client is waiting for a server response in order to establish a connection with it.</p>
     */
    CONNECTING("CONNECTING"),
    /**
     * <p>The client has received a preliminary response from the server and it is currently verifying
     * if a streaming connection is possible.</p>
     */
    STREAM_SENSING("CONNECTED:STREAM-SENSING"),
    /**
     * <p>A streaming connection over WebSocket is active.</p>
     */
    WS_STREAMING("CONNECTED:WS-STREAMING"),
    /**
     * <p>A streaming connection over HTTP is active.</p>
     */
    HTTP_STREAMING("CONNECTED:HTTP-STREAMING"),
    /**
     * <p>A polling connection over WebSocket is in progress.</p>
     */
    WS_POLLING("CONNECTED:WS-POLLING"),
    /**
     * <p>A polling connection over HTTP is in progress.</p>
     */
    HTTP_POLLING("CONNECTED:HTTP-POLLING"),
    /**
     * <p>The server hasn't been sending data over an active streaming connection for longer than a
     * configured time.</p>
     */
    STALLED("STALLED");

    private String mLightStreamerStatus;

    /**
     * <p>Instantiates a client status. In order to work with these statuses, it is necessary to
     * establish which is the equivalent LightStreamer status.</p>
     * @param lsStatus is the equivalent LightStreamer status.
     */
    ClientStatus(String lsStatus) {
        mLightStreamerStatus = lsStatus;
    }

    /**
     * <p>Returns the original status that LightStreamer returned.</p>
     * @return a string containing the original LightStreamer status.
     */
    public String getLightStreamerStatus()
    {
        return mLightStreamerStatus;
    }

    /**
     * <p>Returns the library status representation given its LightStreamer value.</p>
     * @param lightStreamerStatus is the status string that LightStreamer will return.
     * @return the equivalent library status.
     */
    public static ClientStatus fromLSStatus(String lightStreamerStatus)
    {
        ClientStatus returnedStatus = null;
        for (ClientStatus processingStatus: values())
        {
            if (processingStatus.getLightStreamerStatus().equals(lightStreamerStatus))
                returnedStatus = processingStatus;
        }
        return returnedStatus;
    }
}
