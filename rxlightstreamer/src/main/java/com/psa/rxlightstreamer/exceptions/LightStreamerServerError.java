package com.psa.rxlightstreamer.exceptions;

import com.psa.rxlightstreamer.helpers.ServerError;

/**
 * <p>Exception that is thrown when there's a LightStreamer server error.</p>
 * @author Pablo Sánchez Alonso.
 * @version 1.0
 */
public class LightStreamerServerError extends Exception {
    private ServerError mServerError;

    /**
     * <p>Returns the server error that caused the exception to be thrown.</p>
     * @return the server error that caused this exception.
     */
    public ServerError getServerError() {
        return mServerError;
    }

    /**
     * <p>Instantiates a new LightStreamer server error.</p>
     * @param serverError is the server error that caused this exception.
     * @param message is the message that the server returned with it.
     */
    public LightStreamerServerError(ServerError serverError, String message)
    {
        super(message);
        mServerError = serverError;
    }
}
