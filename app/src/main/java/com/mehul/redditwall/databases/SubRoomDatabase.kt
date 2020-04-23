package com.mehul.redditwall.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mehul.redditwall.objects.Subreddit

@Database(entities = [Subreddit::class], version = 3, exportSchema = false)
abstract class SubRoomDatabase : RoomDatabase() {
    abstract fun subDao(): SubDAO

    companion object {
        private var SINGLETON: SubRoomDatabase? = null
        fun getDatabase(context: Context): SubRoomDatabase? {
            if (SINGLETON == null) {
                synchronized(SubRoomDatabase::class.java) {
                    if (SINGLETON == null) {
                        // Create database here
                        SINGLETON = Room.databaseBuilder(context.applicationContext,
                                        SubRoomDatabase::class.java, "sub_database") // Wipes and rebuilds instead of migrating
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