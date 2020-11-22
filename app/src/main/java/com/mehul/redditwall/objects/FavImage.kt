package com.mehul.redditwall.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_table")
class FavImage(@field:PrimaryKey(autoGenerate = true) var id: Int,
               @Expose @field:SerializedName("url")
               @field:ColumnInfo(name = "fav_url") var favUrl: String,
               @Expose @field:SerializedName("gif")
               @field:ColumnInfo(name = "fav_gif") var isGif: Boolean,
               @Expose @field:SerializedName("post")
               @field:ColumnInfo(name = "fav_post_link") var postLink: String,
               @SerializedName("fav_name")
               @field:ColumnInfo(name = "fav_name") var favName: String,
               @Expose @field:SerializedName("preview_url")
               @field:ColumnInfo(name = "preview_url") var previewUrl: String, var selected: Boolean = false)
