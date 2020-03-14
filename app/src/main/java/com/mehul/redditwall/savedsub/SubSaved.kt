package com.mehul.redditwall.savedsub

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sub_table")
class SubSaved(@field:PrimaryKey(autoGenerate = true) var id: Int,
               @field:ColumnInfo(name = "sub_name") var subName: String,
               @field:ColumnInfo(name = "sub_date") var subDate: String) {
    @field:ColumnInfo(name = "sub_int_date")
    var internalDate: String = subDate

    init {
        convertDate()
    }

    private fun convertDate() {
        if (!subDate.contains("at")) {
            return
        }

        val tempDate = subDate.trim().split(" at ")
        val date = tempDate[0].trim().split("-")
        val time = tempDate[1].trim().split(":")
        val month = months[Integer.parseInt(date[0])]
        subDate = "$month ${getOrdinal(Integer.parseInt(date[1]))}, ${date[2]} | ${Integer.parseInt(time[0])}:${time[1]}"

    }

    private fun getOrdinal(n: Int): String {
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

    companion object {
        private val months = arrayOf("INIT", "Jan", "Feb", "March", "April", "May", "June", "July", "Oct", "Nov", "Dec")
        private val ordinals = arrayOf("th", "st", "nd", "rd")
    }
}