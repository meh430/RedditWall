package com.mehul.redditwall.favorites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavImage.class}, version = 1, exportSchema = false)
public abstract class FavRoomDatabase extends RoomDatabase {
    private static FavRoomDatabase SINGLETON;

    static FavRoomDatabase getDatabase(final Context context) {
        if (SINGLETON == null) {
            synchronized (FavRoomDatabase.class) {
                if (SINGLETON == null) {
                    // Create database here
                    SINGLETON = Room.databaseBuilder(context.getApplicationContext(),
                            FavRoomDatabase.class, "fav_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return SINGLETON;
    }

    public abstract FavDAO favDAO();
}