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

    @NonNull
    @ColumnInfo(name = "coin_id")
    private String coinId;

    @NonNull
    @ColumnInfo(name = "exchange")
    private String ex = "international market";

    @NonNull
    @ColumnInfo(name = "currency")
    private String curr = "inr";

    @ColumnInfo(name = "current_price")
    private float cp = 0.0f;

    @ColumnInfo(name = "low_price")
    private float lp = 0.0f;

    @ColumnInfo(name = "high_price")
    private float hp = 0.0f;

    @ColumnInfo(name = "one_time")
    private boolean oneTime = true;

    //true means active, false means already triggered no need to trigger again
    @ColumnInfo(name = "status_low")
    private boolean sl = true;

    @ColumnInfo(name = "status_high")
    private boolean sh = true;

    @ColumnInfo(name = "rank")
    private int rank = 0;


    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull String coinId) {
        this.coinId = coinId;
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

    public float getCp() {
        return cp;
    }

    public void setCp(float cp) {
        this.cp = cp;
    }

    public float getLp() {
        return lp;
    }

    public void setLp(float lp) {
        this.lp = lp;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public boolean isSl() {
        return sl;
    }

    public void setSl(boolean sl) {
        this.sl = sl;
    }

    public boolean isSh() {
        return sh;
    }

    public void setSh(boolean sh) {
        this.sh = sh;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
