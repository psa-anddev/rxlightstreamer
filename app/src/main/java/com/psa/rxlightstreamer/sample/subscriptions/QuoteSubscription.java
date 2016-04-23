package com.psa.rxlightstreamer.sample.subscriptions;

import com.lightstreamer.client.ItemUpdate;
import com.psa.rxlightstreamer.core.RxSubscription;
import com.psa.rxlightstreamer.helpers.SubscriptionType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import rx.Observable;

/**
 * <p>Test subscription.</p>
 * @author Pablo Sanchez Alonso.
 * @version 1.0
 */
public class QuoteSubscription extends RxSubscription<QuoteSubscription.Quote>{
    /**
     * <p>Instantiates a RxSubscription.</p>
     */
    public QuoteSubscription() {
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
    public Observable<SubscriptionEvent<Quote>> getSubscriptionObservable() {
        return mRawObservable
                .map(i -> {
                    Quote quote = null;
                    ItemUpdate itemUpdate = i.getUpdatedItem();
                    try {
                        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
                        decimalFormat.setParseBigDecimal(true);
                        if (itemUpdate != null)
                            quote = new Quote(itemUpdate.getItemName(), itemUpdate.getValue("stock_name"),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("last_price")),
                                    itemUpdate.getValue("time"),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("pct_change")),
                                    Integer.parseInt(itemUpdate.getValue("bid_quantity")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("bid")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("ask")),
                                    Integer.parseInt(itemUpdate.getValue("ask_quantity")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("min")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("max")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("ref_price")),
                                    (BigDecimal) decimalFormat.parse(itemUpdate.getValue("open_price"))
                            );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return new SubscriptionEvent<>(i.isSubscribed(), quote);
                });
    }

    /**
     * <p>Represents a stock market.</p>
     */
    public static class Quote
    {
        private String mId;
        private String mStockName;
        private BigDecimal mLastPrice;
        private String mTime;
        private BigDecimal mChange;
        private int mBidSize;
        private BigDecimal mBid;
        private BigDecimal mAsk;
        private int mAskSize;
        private BigDecimal mMin;
        private BigDecimal mMax;
        private BigDecimal mRef;
        private BigDecimal mOpen;


        /**
         * <p>Instantiates a quote.</p>
         * @param id is the identifier.
         */
        public Quote(String id) {
            mId = id;
        }

        /**
         * <p>Instantiates a new quote.</p>
         * @param id is the item that was subscribed.
         * @param stockName is the stock name.
         * @param lastPrice is the last price.
         * @param time is the time (as a string)
         * @param change is the change.
         * @param bidSize is the bid size.
         * @param bid is the bid.
         * @param ask is the ask.
         * @param askSize is the ask size.
         * @param min is the minimum price.
         * @param max is the maximum price.
         * @param ref is the reference price.
         * @param open is the open price.
         */
        public Quote(String id, String stockName, BigDecimal lastPrice, String time,
                     BigDecimal change, int bidSize, BigDecimal bid, BigDecimal ask,
                     int askSize, BigDecimal min, BigDecimal max, BigDecimal ref,
                     BigDecimal open)
        {
            this(id);
            mStockName = stockName;
            mLastPrice = lastPrice;
            mTime = time;
            mChange = change;
            mBidSize = bidSize;
            mBid = bid;
            mAsk = ask;
            mAskSize = askSize;
            mMin = min;
            mMax = max;
            mRef = ref;
            mOpen = open;
        }

        /**
         * <p>Gets the identifier.</p>
         * @return the identifier of the item that was described as a string.
         */
        public String getId() {
            return mId;
        }

        /**
         * <p>Gets the stock name.</p>
         * @return the stock name.
         */
        public String getStockName() {
            return mStockName;
        }

        /**
         * <p>Returns the last price.</p>
         * @return the last price.
         */
        public BigDecimal getLastPrice() {
            return mLastPrice;
        }

        /**
         * <p>Gets the time the last price was sent as a string.</p>
         * @return the time of the last price.
         */
        public String getTime() {
            return mTime;
        }

        /**
         * <p>Returns the change.</p>
         * @return the change.
         */
        public BigDecimal getChange() {
            return mChange;
        }

        /**
         * <p>Returns the bid size.</p>
         * @return the bid size.
         */
        public int getBidSize() {
            return mBidSize;
        }

        /**
         * <p>Gets the bid.</p>
         * @return the bid.
         */
        public BigDecimal getBid() {
            return mBid;
        }

        /**
         * <p>Returns the ask.</p>
         * @return the ask.
         */
        public BigDecimal getAsk() {
            return mAsk;
        }

        /**
         * <p>Returns the ask size.</p>
         * @return the ask size.
         */
        public int getAskSize() {
            return mAskSize;
        }

        /**
         * <p>Returns the minimum price.</p>
         * @return the minimum price.
         */
        public BigDecimal getMin() {
            return mMin;
        }

        /**
         * <p>Returns the maximum price.</p>
         * @return the maximum price.
         */
        public BigDecimal getMax() {
            return mMax;
        }

        /**
         * <p>Returns the reference price.</p>
         * @return the reference price.
         */
        public BigDecimal getRef() {
            return mRef;
        }

        /**
         * <p>Returns the opening price.</p>
         * @return the opening price.
         */
        public BigDecimal getOpen() {
            return mOpen;
        }

        /**
         * <p>Sets the stock market name.</p>
         * @param stockName the name of the stock market.
         */
        public void setStockName(String stockName) {
            mStockName = stockName;
        }

        /**
         * <p>Sets the last price.</p>
         * @param lastPrice last price to set.
         */
        public void setLastPrice(BigDecimal lastPrice) {
            mLastPrice = lastPrice;
        }

        /**
         * <p>Sets the time for the last price.</p>
         * @param time is the time to set as a string in the format hh:mm:ss
         */
        public void setTime(String time) {
            mTime = time;
        }

        /**
         * <p>Sets the change.</p>
         * @param change is the change to set.
         */
        public void setChange(BigDecimal change) {
            mChange = change;
        }

        /**
         * <p>Sets the bid size.</p>
         * @param bidSize is the bid size to set.
         */
        public void setBidSize(int bidSize) {
            mBidSize = bidSize;
        }

        /**
         * <p>Sets the bid.</p>
         * @param bid bid to set.
         */
        public void setBid(BigDecimal bid) {
            mBid = bid;
        }

        /**
         * <p>Sets the ask.</p>
         * @param ask ask to set.
         */
        public void setAsk(BigDecimal ask) {
            mAsk = ask;
        }

        /**
         * <p>Sets the ask size.</p>
         * @param askSize ask size to set.
         */
        public void setAskSize(int askSize) {
            mAskSize = askSize;
        }

        /**
         * <p>Sets the minimum price.</p>
         * @param min minimum price.
         */
        public void setMin(BigDecimal min) {
            mMin = min;
        }

        /**
         * <p>Sets the maximum price.</p>
         * @param max maximum price.
         */
        public void setMax(BigDecimal max) {
            mMax = max;
        }

        /**
         * <p>Sets the reference price.</p>
         * @param ref reference price to set.
         */
        public void setRef(BigDecimal ref) {
            mRef = ref;
        }

        /**
         * <p>Sets the open price.</p>
         * @param open open price to set.
         */
        public void setOpen(BigDecimal open) {
            mOpen = open;
        }
    }
}
