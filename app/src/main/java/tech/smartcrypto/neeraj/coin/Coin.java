package tech.smartcrypto.neeraj.coin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

/**
 * Created by neeraj on 28/1/18.
 */

@Entity (tableName = "coins")
public class Coin {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "open_price")
    private float openPrice = 0.0f;

    @ColumnInfo(name = "current_price")
    private float currentPrice;

    @ColumnInfo(name = "currency")
    private String currency = "INR";

    //@TypeConverters(DateConverter.class)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
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
}
