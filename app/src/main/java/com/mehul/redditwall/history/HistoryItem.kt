package com.mehul.redditwall.history

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hist_table")
class HistoryItem(@field:PrimaryKey(autoGenerate = true) var id: Int,
                  @field:ColumnInfo(name = "hist_name") var subName: String,
                  @field:ColumnInfo(name = "hist_date") var setDate: String,
                  @field:ColumnInfo(name = "hist_source") var source: Int,
                  @field:ColumnInfo(name = "hist_url") var url: String,
                  @field:ColumnInfo(name = "hist_post_link") var postLink: String) {
    @field:ColumnInfo(name = "hist_int_date")
    var internalDate: String = setDate

    init {
        Log.e("INTERNAL", internalDate)
        convertDate()
    }

    private fun convertDate() {
        if (!setDate.contains("at")) {
            return
        }

        val tempDate = setDate.trim().split(" at ")
        val date = tempDate[0].trim().split("-")
        val time = tempDate[1].trim().split(":")
        val month = months[Integer.parseInt(date[0])]
        var hours = Integer.parseInt(time[0])
        val pmam = if (hours > 12) {
            hours -= 12
            "p.m"
        } else {
            "a.m"
        }
        setDate = "$month ${getOrdinal(Integer.parseInt(date[1]))}, ${date[2]} | ${hours}:${time[1]} $pmam"
        Log.e("DATE", setDate)

    }

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