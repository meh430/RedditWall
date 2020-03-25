package com.mehul.redditwall

import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.favorites.FavRepository
import com.mehul.redditwall.history.HistoryItem
import com.mehul.redditwall.history.HistoryRepository
import java.text.SimpleDateFormat
import java.util.*

class ChangeWallpaper : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "CHANGE_WALL") {
            //change wallpaper
            changeWall(context)
        }
    }

    companion object {
        fun changeWall(con: Context) {
            val pref = con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
            val width = pref!!.getInt(SettingsActivity.IMG_WIDTH, 1920)
            val height = pref.getInt(SettingsActivity.IMG_HEIGHT, 1080)
            val favs = FavRepository(con).favAsList
            if (favs!!.isEmpty()) {
                return
            }

            var randomNum = (0..favs.size).random()
            while (randomNum >= favs.size || randomNum < 0) {
                randomNum = (0..favs.size).random()
            }
            if (randomNum == favs.size) randomNum = favs.size - 1
            val current = favs[randomNum]

            val wall: WallpaperManager? = con.applicationContext.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
            Glide.with(con)
                    .asBitmap()
                    .load(current?.favUrl)
                    .override(width, height)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            wall?.setBitmap(resource)
                            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, current!!.favName,
                                    SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date()),
                                    HistoryItem.REFRESH, current.favUrl, current.postLink)
                            HistoryRepository(con).insert(histItem)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })


        }
    }
}
