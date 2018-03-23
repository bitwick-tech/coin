package tech.smartcrypto.neeraj.coin;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by neeraj on 28/1/18.
 */

@Entity(tableName = "coins_static_data", primaryKeys = {"_id", "ex", "curr"})
public class CoinStaticData {

    @ColumnInfo(name = "_id")
    @NonNull
    private String coinId;

    @ColumnInfo(name = "ex")
    @NonNull
    private String ex = "international market";

    @ColumnInfo(name = "curr")
    @NonNull
    private String curr = "INR";

    @ColumnInfo(name = "c_name")
    private String coinName;


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

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}
