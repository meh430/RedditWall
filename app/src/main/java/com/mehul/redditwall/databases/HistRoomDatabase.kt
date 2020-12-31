package com.mehul.redditwall.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mehul.redditwall.objects.HistoryItem

@Database(entities = [HistoryItem::class], version = 4, exportSchema = false)
abstract class HistRoomDatabase : RoomDatabase() {
    abstract fun historyDAO(): HistoryDAO

    companion object {
        private var SINGLETON: HistRoomDatabase? = null
        fun getDatabase(context: Context): HistRoomDatabase? {
            if (SINGLETON == null) {
                synchronized(HistRoomDatabase::class.java) {
                    if (SINGLETON == null) {
                        // Create database here
                        SINGLETON = Room.databaseBuilder(context.applicationContext,
                                HistRoomDatabase::class.java, "hist_database") // Wipes and rebuilds instead of migrating
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