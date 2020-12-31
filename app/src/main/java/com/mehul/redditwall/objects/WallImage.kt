package com.mehul.redditwall.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_table")
class WallImage(@field:PrimaryKey(autoGenerate = true) var id: Int = 0,
                @field:ColumnInfo(name = "fav_url") var imgUrl: String = "",
                @field:ColumnInfo(name = "fav_post_link") var postLink: String = "",
                @field:ColumnInfo(name = "fav_name") var subName: String = "",
                @field:ColumnInfo(name = "preview_url") var previewUrl: String = "")
