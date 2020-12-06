package com.mehul.redditwall.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mehul.redditwall.objects.Subreddit

@Dao
interface SubDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(saved: Subreddit)

    @Query("DELETE FROM sub_table")
    fun deleteAll()

    @get:Query("SELECT * from sub_table ORDER BY sub_name ASC")
    val allSubreddit: LiveData<List<Subreddit>>

    @get:Query("SELECT * from sub_table LIMIT 1")
    val anySubreddit: Array<Subreddit>

    @Delete
    fun deleteSubreddit(saved: Subreddit)
}