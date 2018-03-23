package tech.smartcrypto.neeraj.coin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


@Entity(tableName = "coins_dynamic_user_data", primaryKeys = {"_id", "ex", "curr"})
public class CoinDynamicUserData {

    @ColumnInfo(name = "_id")
    @NonNull
    private String coinId;

    @ColumnInfo(name = "ex")
    @NonNull
    private String ex = "international market";

    @ColumnInfo(name = "curr")
    @NonNull
    private String curr = "INR";

//    @ColumnInfo(name = "watchlist_count")
//    private boolean wc = false;
//
//    @ColumnInfo(name = "alert_count")
//    private int ac = 0;

//    @ColumnInfo(name = "rank_coin")
//    private int rc = 0;
//
//    @ColumnInfo(name = "rank_exchange")
//    private float re = 0;


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

//    public boolean getWc() {
//        return wc;
//    }
//
//    public void setWc(boolean wc) {
//        this.wc = wc;
//    }
//
//    public int getAc() {
//        return ac;
//    }
//
//    public void setAc(int ac) {
//        this.ac = ac;
//    }

//    public int getRc() {
//        return rc;
//    }
//
//    public void setRc(int rc) {
//        this.rc = rc;
//    }
//
//    public float getRe() {
//        return re;
//    }
//
//    public void setRe(float re) {
//        this.re = re;
//    }
}
