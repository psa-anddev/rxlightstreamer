package com.psa.rxlightstreamer.helpers;

/**
 * <p>Represents the type of subscription.</p>
 */
public enum SubscriptionType {
    MERGE("MERGE"),
    DISTINCT("DISTINCT"),
    RAW("RAW"),
    COMMAND("COMMAND");

    private String mLSSubscriptionType;

    /**
     * <p>Instantiates a new subscription type.</p>
     * @param LSSubscriptionType is the type of subscription that LS understands.
     */
    SubscriptionType(String LSSubscriptionType) {
        mLSSubscriptionType = LSSubscriptionType;
    }

    /**
     * <p>Returns the subscription type as returned by LS.</p>
     * @return a string containing the subscription type.
     */
    public String getLSSubscriptionType() {
        return mLSSubscriptionType;
    }
}
