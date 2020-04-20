package com.mehul.redditwall.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mehul.redditwall.objects.FavImage

@Database(entities = [FavImage::class], version = 4, exportSchema = false)
abstract class FavRoomDatabase : RoomDatabase() {
    abstract fun favDAO(): FavDAO

    companion object {
        @Volatile
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