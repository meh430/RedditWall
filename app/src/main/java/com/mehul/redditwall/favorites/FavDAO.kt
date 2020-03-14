package com.mehul.redditwall.favorites

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saved: FavImage?)

    @Query("DELETE FROM fav_table")
    fun deleteAll()

    @get:Query("SELECT * from fav_table ORDER BY fav_url ASC")
    val allFavImages: LiveData<List<FavImage?>?>?

    @get:Query("SELECT * from fav_table ORDER BY fav_url ASC")
    val favAsList: List<FavImage?>?

    @Delete
    fun deleteFavImage(saved: FavImage?)
}