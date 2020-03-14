package com.mehul.redditwall.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_table")
class FavImage(@field:PrimaryKey(autoGenerate = true) var id: Int,
               @field:ColumnInfo(name = "fav_url") var favUrl: String,
               @field:ColumnInfo(name = "fav_gif") var isGif: Boolean,
               @field:ColumnInfo(name = "fav_post_link") var postLink: String)