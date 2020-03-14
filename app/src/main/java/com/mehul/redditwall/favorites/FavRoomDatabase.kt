package com.mehul.redditwall.favorites

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavImage::class], version = 3, exportSchema = false)
abstract class FavRoomDatabase : RoomDatabase() {
    abstract fun favDAO(): FavDAO?

    companion object {
        private var SINGLETON: FavRoomDatabase? = null
        fun getDatabase(context: Context): FavRoomDatabase? {
            if (SINGLETON == null) {
                synchronized(FavRoomDatabase::class.java) {
                    if (SINGLETON == null) {
                        // Create database here
                        SINGLETON = Room.databaseBuilder(context.applicationContext,
                                        FavRoomDatabase::class.java, "fav_database") // Wipes and rebuilds instead of migrating
                                // if no Migration object.
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return SINGLETON
        }
    }
}