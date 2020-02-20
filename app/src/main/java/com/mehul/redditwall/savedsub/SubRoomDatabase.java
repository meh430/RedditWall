package com.mehul.redditwall.savedsub;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SubSaved.class}, version = 1, exportSchema = false)
public abstract class SubRoomDatabase extends RoomDatabase {
    private static SubRoomDatabase SINGLETON;

    static SubRoomDatabase getDatabase(final Context context) {
        if (SINGLETON == null) {
            synchronized (SubRoomDatabase.class) {
                if (SINGLETON == null) {
                    // Create database here
                    SINGLETON = Room.databaseBuilder(context.getApplicationContext(),
                            SubRoomDatabase.class, "sub_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return SINGLETON;
    }

    public abstract SubDAO subDao();
}
