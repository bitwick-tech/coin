package utils;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by neerajlajpal on 01/02/18.
 */

public class DatabaseHandler {
    private static AppDatabase db;
    private DatabaseHandler(){}
    public static AppDatabase getInstance(Context context){
        if(db == null) db = Room.databaseBuilder(context,
                AppDatabase.class, "appDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        //TODO remove allowMainThreadQueris and use loadermanager in AlertDialogFragment.java
        return db;
    }

}
