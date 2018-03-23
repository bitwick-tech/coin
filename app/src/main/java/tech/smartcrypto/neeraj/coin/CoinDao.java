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
public interface CoinDao {
    @Query("SELECT * FROM coins")
    LiveData<List<Coin>> getAll();

    @Query("SELECT _id, ex, curr FROM coins")
    List<CoinIdExCurr> getAllIds();

    @Query("SELECT _id, ex, curr FROM coins WHERE _id IS (:coinId)")
    List<CoinIdExCurr> getSingleId(String coinId);

    @Query("SELECT DISTINCT(_id) FROM coins")
    List<String> getAllCoinIds();

    @Query("SELECT * FROM coins WHERE _id IS (:coinId)")
    LiveData<List<Coin>> getAllByCoinId(String coinId);

    @Query("SELECT * FROM coins")
    List<Coin> getAllList();

    @Query("SELECT * FROM coins WHERE _id IN (:coinIds)")
    List<Coin> loadAllByIds(int[] coinIds);

    @Query("SELECT * FROM coins WHERE _id IS (:coinId)")
    Coin loadAById(int[] coinId);

//    @Query("SELECT * FROM Coin WHERE first_name LIKE :first AND "
//            + "last_name LIKE :last LIMIT 1")
//    Coin findByName(String first, String last);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Coin... Coins);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertOne(Coin coin);

    @Delete
    void delete(Coin Coin);

    @Delete
    void deleteMany(Coin... coins);

    @Query("DELETE FROM coins WHERE _id =(:coinId)")
    void deleteByCoinId(String coinId);

    @Update
    void updateCoins(Coin... coins);

    class CoinIdExCurr {
        @ColumnInfo(name="_id")
        public String coinId;
        @ColumnInfo(name="ex")
        public String ex;
        @ColumnInfo(name="curr")
        public String curr;
    }
}
