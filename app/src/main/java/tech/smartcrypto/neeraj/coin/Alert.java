package tech.smartcrypto.neeraj.coin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by neerajlajpal on 01/02/18.
 */

@Entity (tableName = "alerts")
public class Alert {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @NonNull
    private int id;

    @ColumnInfo(name = "coin_id")
    private String coinId;

    @ColumnInfo(name = "coin_name")
    private String coinName;

    @ColumnInfo(name = "low_price")
    private float lowPrice = 0.0f;

    @ColumnInfo(name = "high_price")
    private float highPrice = 0.0f;

    @ColumnInfo(name = "current_price")
    private float currentPrice;

    @ColumnInfo(name = "currency")
    private String currency;

    @ColumnInfo(name = "one_time")
    private boolean oneTime = true;

    //true means active, false means already triggered no need to trigger again
    @ColumnInfo(name = "status_min")
    private boolean statusMin = true;

    @ColumnInfo(name = "status_max")
    private boolean statusMax = true;



    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public boolean isStatusMin() {
        return statusMin;
    }

    public void setStatusMin(boolean statusMin) {
        this.statusMin = statusMin;
    }

    public boolean isStatusMax() {
        return statusMax;
    }

    public void setStatusMax(boolean statusMax) {
        this.statusMax = statusMax;
    }
}
