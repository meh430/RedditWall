package com.mehul.redditwall.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mehul.redditwall.objects.WallImage

@Dao
interface FavDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(saved: WallImage)

    @Query("DELETE FROM fav_table")
    fun deleteAll()

    @get:Query("SELECT * from fav_table ORDER BY fav_url ASC")
    val allFavImages: LiveData<List<WallImage>>

    @get:Query("SELECT * from fav_table ORDER BY fav_url ASC")
    val favAsList: List<WallImage>

    @Delete
    fun deleteFavImage(saved: WallImage)
}