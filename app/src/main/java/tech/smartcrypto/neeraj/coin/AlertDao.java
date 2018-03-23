package tech.smartcrypto.neeraj.coin;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by neerajlajpal on 01/02/18.
 */

@Dao
public interface AlertDao {

    @Query("SELECT * FROM alerts")
    LiveData<List<Alert>> getAll();

    @Query("SELECT DISTINCT coin_id, exchange, currency FROM alerts")
    List<CoinIdExCurr> getAllCoinIdExCurr();

    @Query("SELECT * FROM alerts")
    List<Alert> getAllList();

    @Query("SELECT * FROM alerts WHERE _id IN (:AlertIds)")
    List<Alert> loadAllByIds(int[] AlertIds);

    @Query("SELECT * FROM alerts WHERE _id IS (:AlertId)")
    Alert loadAById(int AlertId);

    @Query("SELECT coin_id FROM alerts")
    List<String> getAllCoinIds();

    @Query("UPDATE alerts SET current_price=(:cp) WHERE coin_id=(:coinId) AND exchange=(:ex) AND currency=(:curr)")
    void updateCoinPrice(float cp, String coinId, String ex, String curr);

    /**
     @Query("SELECT * FROM Alert WHERE first_name LIKE :first AND "
     + "last_name LIKE :last LIMIT 1")
     Alert findByName(String first, String last);
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Alert... Alerts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(Alert alert);

    @Delete
    void delete(Alert Alert);

    @Update
    void updateAlerts(Alert... alerts);

    @Update
    void updateAlert(Alert alert);

    class CoinIdExCurr {
        @ColumnInfo(name="coin_id")
        public String coinId;
        @ColumnInfo(name="exchange")
        public String ex;
        @ColumnInfo(name="currency")
        public String curr;
    }

}
