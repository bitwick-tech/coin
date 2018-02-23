package tech.smartcrypto.neeraj.coin;

import android.arch.lifecycle.LiveData;
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

    @Query("SELECT * FROM coins")
    List<Coin> getAllList();

    @Query("SELECT * FROM coins WHERE _id IN (:CoinIds)")
    List<Coin> loadAllByIds(int[] CoinIds);

    @Query("SELECT * FROM coins WHERE _id IS (:CoinId)")
    Coin loadAById(int[] CoinId);

    /**
    @Query("SELECT * FROM Coin WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    Coin findByName(String first, String last);
    */

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Coin... Coins);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertOne(Coin coin);

    @Delete
    void delete(Coin Coin);

    @Update
    void updateCoins(Coin... coins);

}
