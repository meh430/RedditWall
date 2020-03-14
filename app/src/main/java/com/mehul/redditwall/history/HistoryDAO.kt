package com.mehul.redditwall.history

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: HistoryItem?)

    @Query("DELETE FROM hist_table")
    fun deleteAll()

    @get:Query("SELECT * from hist_table ORDER BY hist_name ASC")
    val allHistoryItems: LiveData<List<HistoryItem?>?>?

    @get:Query("SELECT * from hist_table LIMIT 1")
    val anyHistoryItem: Array<HistoryItem?>?

    @Delete
    fun deleteHistoryItem(history: HistoryItem?)
}