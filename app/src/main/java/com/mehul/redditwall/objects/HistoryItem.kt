package com.mehul.redditwall.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hist_table")
class HistoryItem(@field:PrimaryKey(autoGenerate = true) var id: Int = 0,
                  @field:ColumnInfo(name = "hist_name") var subName: String,
                  @field:ColumnInfo(name = "hist_date") var setDate: Long,
                  @field:ColumnInfo(name = "hist_source") var source: Int,
                  @field:ColumnInfo(name = "hist_url") var imgUrl: String,
                  @field:ColumnInfo(name = "hist_preview") var previewUrl: String,
                  @field:ColumnInfo(name = "hist_post_link") var postLink: String) {

    companion object {
        val months = arrayOf("INIT", "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
        private val ordinals = arrayOf("th", "st", "nd", "rd")
        val sources = arrayOf("Downloaded", "Set on home screen", "Set on lock screen", "Set on both", "Set through refresh")
        const val DOWNLOADED = 0
        const val HOME_SCREEN = 1
        const val LOCK_SCREEN = 2
        const val BOTH = 3
        const val REFRESH = 4

        fun getOrdinal(n: Int): String {
            val suffix = if (n > 0) {
                ordinals[if (n in 4..20 || n % 10 > 3) {
                    0
                } else {
                    n % 10
                }]
            } else {
                ""
            }
            return "$n$suffix"
        }
    }
}