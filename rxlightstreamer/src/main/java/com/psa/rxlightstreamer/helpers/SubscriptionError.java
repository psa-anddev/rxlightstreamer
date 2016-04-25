package com.psa.rxlightstreamer.helpers;

/**
 * <p>This enum represents the different subscription errors.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public enum SubscriptionError {
    /**
     * <p>Bad data adapter name or no default data adapter defined.</p>
     */
    BAD_DATA_ADAPTER_NAME(17),
    /**
     * <p>Session was interrupted.</p>
     */
    SESSION_INTERRUPTED(20),
    /**
     * <p>Bad group name.</p>
     */
    BAD_GROUP_NAME(21),
    /**
     * <p>Bad group name fo this schema.</p>
     */
    BAD_GROUP_NAME_FOR_SCHEMA(22),
    /**
     * <p>Bad schema name.</p>
     */
    BAD_SCHEMA_NAME(23),
    /**
     * <p>Mode not allowed for an item.</p>
     */
    MODE_NOT_ALLOWED_FOR_ITEM(24),
    /**
     * <p>Bad selector name.</p>
     */
    BAD_SELECTOR_NAME(25),
    /**
     * <p>Unfiltered dispatching not allowed for an Item, because a frequency limit is associated
     * to the item</p>
     */
    UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_FREQ_LIMIT(26),
    /**
     * <p>Unfiltered dispatching not supported for an Item, because a frequency prefiltering is
     * applied for the item.</p>
     */
    UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_FREQ_PREFILTERING(27),
    /**
     * <p>Unfiltered dispatching is not allowed by the current license terms
     * (for special licenses only)</p>
     */
    UNFILTERED_DISPATCHING_NOT_ALLOWED_DUE_TO_LICENCE_TERMS(28),
    /**
     * <p>Raw mode not allowed due to licence terms.</p>
     */
    RAW_MODE_NOT_ALLOWED(29),
    /**
     * <p>Subscription not allowed due to licence terms.</p>
     */
    SUBSCRIPTION_NOT_ALLOWED(30),
    /**
     * <p>the Metadata Adapter has refused the subscription or unsubscription request;
     * the code value is dependent on the specific Metadata Adapter implementation</p>
     */
    SUBSCRIPTION_OR_UNSUBSCRIPTION_REQUEST_REJECTED(-1);

    private int mCode;

    SubscriptionError(int code)
    {
        mCode = code;
    }

    /**
     * <p>Returns the LS code.</p>
     * @return the LS error code.
     */
    public int getCode() {
        return mCode;
    }

    /**
     * <p>Returns the subscription error that corresponds to the given error code.</p>
     * @param code LS error code.
     * @return Subscription error object corresponding to the given code.
     */
    public static SubscriptionError fromLSCode(int code)
    {
        SubscriptionError returnedSubscriptionError = null;
        if (code < 0)
        {
            returnedSubscriptionError = SUBSCRIPTION_OR_UNSUBSCRIPTION_REQUEST_REJECTED;
            returnedSubscriptionError.mCode = code;
        }
        else
            for (SubscriptionError processedSubscriptionError : values())
                if (code == processedSubscriptionError.getCode())
                    returnedSubscriptionError = processedSubscriptionError;
        return returnedSubscriptionError;
    }
}
