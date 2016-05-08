package com.psa.rxlightstreamer.sample.subscriptions;

import com.lightstreamer.ls_client.UpdateInfo;
import com.psa.rxlightstreamer.core.RxNonUnifiedSubscription;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.SubscriptionType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import rx.Observable;

/**
 * <p>This class is a non unified quote subscription.</p>
 *
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class QuoteNonUnifiedSubscription extends RxNonUnifiedSubscription<QuoteSubscription.Quote>
{
    /**
     * <p>Instantiates a subscription.</p>
     */
    public QuoteNonUnifiedSubscription()
    {
        super(SubscriptionType.MERGE, "QUOTE_ADAPTER",
                new String[]{
                        "stock_name", "last_price", "time", "pct_change", "bid_quantity",
                        "bid", "ask", "ask_quantity", "min", "max", "ref_price", "open_price"
                },
                new String[]{
                        "item1", "item2",
                        "item3", "item4",
                        "item5", "item6",
                        "item7", "item8",
                        "item9", "item10"
                },
                true);
    }

    @Override
    public Observable<RxSubscription.SubscriptionEvent<QuoteSubscription.Quote>> getSubscriptionObservable()
    {
        return mRawObservable
                .map(i -> {
                    QuoteSubscription.Quote quote = null;
                    UpdateInfo itemUpdate = i.getUpdatedItem();
                    try
                    {
                        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
                        decimalFormat.setParseBigDecimal(true);
                        if (itemUpdate != null)
                            quote = new QuoteSubscription.Quote(itemUpdate.getItemName(), getValue(itemUpdate, "stock_name"),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "last_price")),
                                    getValue(itemUpdate, "time"),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "pct_change")),
                                    Integer.parseInt(getValue(itemUpdate, "bid_quantity")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "bid")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "ask")),
                                    Integer.parseInt(getValue(itemUpdate, "ask_quantity")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "min")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "max")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "ref_price")),
                                    (BigDecimal) decimalFormat.parse(getValue(itemUpdate, "open_price"))
                            );
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    return new RxSubscription.SubscriptionEvent<>(i.isSubscribed(), quote);
                });
    }
    
    /**
     * <p>Method that check if the value has changed for a field and returns the right value.</p>
     *
     * @param updateInfo is the update information.
     * @param key        is the key of the value in the update.
     * @return the actual value.
     */
    private String getValue(UpdateInfo updateInfo, String key)
    {
        return updateInfo.isValueChanged(key) ? updateInfo.getNewValue(key) : updateInfo.getOldValue(key);
    }
}
