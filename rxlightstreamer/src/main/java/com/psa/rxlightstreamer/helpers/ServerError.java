package com.psa.rxlightstreamer.helpers;

/**
 * <p>This enum holds the reasons why the LightStreamer server can terminate a connection.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public enum ServerError {
    /**
     * <p>the Metadata Adapter has refused the user connection;
     * the code value is dependent on the specific Metadata Adapter implementation</p>
     */
    REFUSED_BY_METADATA_ADAPTER(-1),

    /**
     * <p>username/password check failed.</p>
     */
    USERNAME_PASSWORD_CHECK_FAILED(1),
    /**
     * <p>The requested adapter set is not available.</p>
     */
    REQUESTED_ADAPTER_SET_NOT_AVAILABLE(2),
    /**
     * <p>The maximum number of licenced sessions has been reached.</p>
     */
    MAXIMUM_LICENCED_SESSION_NUMBER_REACHED(7),
    /**
     * <p>The maximum number of sessions configured for this adapter set has been reached.</p>
     */
    MAXIMUM_CONFIGURED_SESSION_NUMBER_REACHED(8),
    /**
     * <p>Configured maximum server load reached</p>
     */
    MAXIUMUM_CONFIGURED_SERVER_LOAD_REACHED(9),
    /**
     * <p>New sessions have been blocked temporally.</p>
     */
    NEW_SESSIONS_TEMPORALLY_BLOCKED(10),
    /**
     * <p>Streaming is not available due to licence restrictions.</p>
     */
    STREAMING_NOT_AVAILABLE(11),

    /**
     * <p>The connection was closed by a destroy request.</p>
     */
    CONNECTION_CLOSED_BY_DESTROY_REQUEST(31),
    /**
     * <p>The connection was closed by JMX.</p>
     */
    CONNECTION_CLOSED_BY_JMX(32),
    /**
     * <p>An unexpected error occurred on the Server while the session was in activity</p>
     */
    UNEXPECTED_ERROR_WHILE_SERVER_WAS_IN_ACTIVITY(33),
    /**
     * <p>The Metadata Adapter imposes limits on the overall open sessions for the current user and
     * has requested the closure of the current session upon opening of a new session for the same
     * user on a different browser window</p>
     */
    CONNECTION_CLOSED_DUE_TO_USER_SESSION_LIMIT_REACHED(35),
    /**
     * <p>Unknown or unexpected error.</p>
     */
    UNKNOWN_OR_UNEXPECTED_ERROR(36),
    /**
     * <p>There was an error in the parsing of the server response thus the client cannot
     * continue with the current session.</p>
     */
    SERVER_RESPONSE_PARSING_ERROR(61);

    /**
     * <p>Holds the code that was returned by the server.</p>
     */
    private int mCode;

    /**
     * <p>Instantiating a server error requires the code returned by LightStreamer.</p>
     * @param code is the code returned by LightStreamer.
     */
    ServerError(int code)
    {
        mCode = code;
    }

    /**
     * <p>Returns the LightStreamer error code.</p>
     * @return the LightStreamer error code as an integer.
     */
    public int getCode() {
        return mCode;
    }

    /**
     * <p>Sets the LightStreamer error code. </p>
     * @param code is the code to set.
     */
    private void setCode(int code) {
        mCode = code;
    }

    /**
     * <p>Converts the code returned by LS into a server error reason the application
     * can understand.</p>
     * @param code is the code that LightStreamer has returned.
     * @return a server error object pointing out the right server error reason.
     */
    public static ServerError fromLSCode(int code)
    {
        ServerError returnedServerError = null;
        if (code < 0)
        {
            returnedServerError = REFUSED_BY_METADATA_ADAPTER;
            returnedServerError.setCode(code);
        }
        else if (code == 33 || code == 34)
        {
            returnedServerError = UNEXPECTED_ERROR_WHILE_SERVER_WAS_IN_ACTIVITY;
            returnedServerError.setCode(code);
        }
        else if (code >=36 && code <= 39)
        {
            returnedServerError = UNKNOWN_OR_UNEXPECTED_ERROR;
            returnedServerError.setCode(code);
        }
        else
        {
            for (ServerError processedError: values())
            {
                if (processedError.getCode() == code)
                    returnedServerError = processedError;
            }
        }

        return returnedServerError;
    }
}
