//package tech.smartcrypto.neeraj.coin;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;
//
//import java.util.List;
//
///**
// * Created by neerajlajpal on 24/02/18.
// */
//
//@Dao
//public interface CoinDynamicUserDataDao {
//    @Query("SELECT * FROM coins_dynamic_user_data")
//    LiveData<List<CoinDynamicUserData>> getAll();
//
//    //TODO change INR to user default currency from sharedPreferences
//    @Query("SELECT * FROM coins_dynamic_user_data WHERE _id IS (:coinId) AND curr IS 'INR'")
//    LiveData<List<CoinDynamicUserData>> getAllByCoinId(String coinId);
////
////    @Query("SELECT * FROM coins_dynamic_user_data")
////    List<CoinDynamicUserData> getAllList();
////
////    @Query("SELECT * FROM coins_dynamic_user_data WHERE _id IN (:CoinIds)")
////    List<CoinDynamicUserData> loadAllByIds(int[] CoinIds);
////
////    @Query("SELECT * FROM coins_dynamic_user_data WHERE _id IS (:CoinId)")
////    CoinDynamicUserData loadAById(int[] CoinId);
////
////    /**
////     @Query("SELECT * FROM Coin WHERE first_name LIKE :first AND "
////     + "last_name LIKE :last LIMIT 1")
////     Coin findByName(String first, String last);
////     */
////
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(CoinDynamicUserData... Coins);
////
////    @Insert (onConflict = OnConflictStrategy.REPLACE)
////    void insertOne(CoinDynamicUserData coin);
////
//    @Delete
//    void delete(CoinDynamicUserData coin);
//
//    @Delete
//    void deleteMany(CoinDynamicUserData... coins);
//
//
//    @Update
//    void updateCoins(CoinDynamicUserData... coins);
//}
