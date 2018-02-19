package utils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import tech.smartcrypto.neeraj.coin.Alert;
import tech.smartcrypto.neeraj.coin.AlertDao;
import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.CoinDao;

/**
 * Created by neerajlajpal on 01/02/18.
 */

/** @Database(version = 1, entities = {Coin.class, Alert.class})
//@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase() {
    // CoinDao is a class annotated with @Dao.
    public abstract CoinDao coinDao();
    // AlertDao is a class annotated with @Dao.
    abstract public AlertDao alertDao();
}
*/


@Database(entities = {Coin.class, Alert.class}, version = 14)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();
    public abstract AlertDao alertDao();
}
