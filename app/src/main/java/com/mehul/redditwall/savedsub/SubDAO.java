package com.mehul.redditwall.savedsub;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SubSaved saved);

    @Query("DELETE FROM sub_table")
    void deleteAll();

    @Query("SELECT * from sub_table ORDER BY sub_name ASC")
    LiveData<List<SubSaved>> getAllSubSaved();

    @Query("SELECT * from sub_table LIMIT 1")
    SubSaved[] getAnySubSaved();

    @Delete
    void deleteSubSaved(SubSaved saved);
}
