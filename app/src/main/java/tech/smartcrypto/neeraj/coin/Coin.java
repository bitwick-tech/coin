package tech.smartcrypto.neeraj.coin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

/**
 * Created by neeraj on 28/1/18.
 */

@Entity (tableName = "coins", primaryKeys = {"_id", "ex", "curr"})
public class Coin {

    @ColumnInfo(name = "_id")
    @NonNull
    private String cId;

    @ColumnInfo(name = "ex")
    @NonNull
    private String ex = "international market";

    @ColumnInfo(name = "curr")
    @NonNull
    private String curr = "INR";

    @ColumnInfo(name = "open_price")
    private float op = 0.0f;

    @ColumnInfo(name = "current_price")
    private float cp = 0.0f;

    @ColumnInfo(name = "vol_24hr")
    private float vol = 0.0f;

    @ColumnInfo(name = "vol_24hr_count")
    private float volCount = 0.0f;

    @ColumnInfo(name = "high_price_24hr")
    private float hp = 0.0f;

    @ColumnInfo(name = "low_price_24hr")
    private float lp = 0.0f;

    @NonNull
    public String getCId() {
        return cId;
    }

    public void setCId(@NonNull String cId) {
        this.cId = cId;
    }

    @NonNull
    public String getEx() {
        return ex;
    }

    public void setEx(@NonNull String ex) {
        this.ex = ex;
    }

    @NonNull
    public String getCurr() {
        return curr;
    }

    public void setCurr(@NonNull String curr) {
        this.curr = curr;
    }

    public float getOp() {
        return op;
    }

    public void setOp(float op) {
        this.op = op;
    }

    public float getCp() {
        return cp;
    }

    public void setCp(float cp) {
        this.cp = cp;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public float getVolCount() {
        return volCount;
    }

    public void setVolCount(float volCount) {
        this.volCount = volCount;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getLp() {
        return lp;
    }

    public void setLp(float lp) {
        this.lp = lp;
    }
}
