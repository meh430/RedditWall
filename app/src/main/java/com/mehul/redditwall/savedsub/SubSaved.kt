package com.mehul.redditwall.savedsub

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_table")
class SubSaved(@field:PrimaryKey(autoGenerate = true) var id: Int,
               @field:ColumnInfo(name = "sub_name") var subName: String,
               @field:ColumnInfo(name = "sub_date") var subDate: String)