package com.mehul.redditwall.databases

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mehul.redditwall.objects.SubSaved

@Dao
interface SubDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(saved: SubSaved?)

    @Query("DELETE FROM sub_table")
    fun deleteAll()

    @get:Query("SELECT * from sub_table ORDER BY sub_name ASC")
    val allSubSaved: LiveData<List<SubSaved?>?>?

    @get:Query("SELECT * from sub_table LIMIT 1")
    val anySubSaved: Array<SubSaved?>?

    @Delete
    fun deleteSubSaved(saved: SubSaved?)
}