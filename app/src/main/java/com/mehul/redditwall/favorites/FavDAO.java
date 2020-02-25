package com.mehul.redditwall.favorites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavImage saved);

    @Query("DELETE FROM fav_table")
    void deleteAll();

    @Query("SELECT * from fav_table ORDER BY fav_url ASC")
    LiveData<List<FavImage>> getAllFavImages();

    @Query("SELECT * from fav_table ORDER BY fav_url ASC")
    List<FavImage> getFavAsList();

    @Delete
    void deleteFavImage(FavImage saved);
}

