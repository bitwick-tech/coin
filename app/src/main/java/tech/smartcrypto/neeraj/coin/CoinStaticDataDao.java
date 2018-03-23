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
 * Created by neerajlajpal on 24/02/18.
 */

@Dao
public interface CoinStaticDataDao {
    @Query("SELECT * FROM coins_static_data")
    LiveData<List<CoinStaticData>> getAll();

    @Query("SELECT * FROM coins_static_data")
    List<CoinStaticData> getAllList();

    @Query("SELECT DISTINCT(_id), c_name FROM coins_static_data")
    List<CoinIdAndName> getAllCoinIdAndNameList();

    @Query("SELECT DISTINCT(_id), c_name FROM coins_static_data WHERE _id IN (:coinIds)")
    List<CoinIdAndName> getCoinIdAndNameListByIds(String[] coinIds);

    @Query("SELECT * FROM coins_static_data WHERE _id IN (:coinIds)")
    List<CoinStaticData> loadAllByIds(String[] coinIds);

    @Query("SELECT * FROM coins_static_data WHERE _id IS (:coinId)")
    CoinStaticData loadAById(String coinId);

    @Query("SELECT DISTINCT(ex) FROM coins_static_data WHERE _id IS (:coinId)")
    LiveData<List<String>> getAllExchangesOfACoin(String coinId);

    @Query("SELECT DISTINCT(ex) FROM coins_static_data WHERE _id IS (:coinId)")
    List<String> getAllExchangesOfACoinList(String coinId);


    @Query("SELECT DISTINCT(c_name) FROM coins_static_data WHERE _id IS (:coinId)")
    String getNameById(String coinId);

    /**
     @Query("SELECT * FROM Coin WHERE first_name LIKE :first AND "
     + "last_name LIKE :last LIMIT 1")
     Coin findByName(String first, String last);
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CoinStaticData... coins);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertOne(CoinStaticData coin);

    @Delete
    void delete(CoinStaticData coin);

    @Update
    void updateCoins(CoinStaticData... coins);

    class CoinIdAndName {
        @ColumnInfo(name="_id")
        public String coinId;
        @ColumnInfo(name="c_name")
        public String coinName;
    }
}
