package com.mehul.redditwall.favorites


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.mehul.redditwall.MainActivity
import com.mehul.redditwall.R
import com.mehul.redditwall.SettingsActivity
import com.mehul.redditwall.WallActivity

class FavAdapter(private val con: Context) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(con)
    private var favs: List<FavImage>? = null
    private val width: Int
    private val height: Int
    private val scale: Int

    init {
        val displayMetrics = DisplayMetrics()
        (con as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
        scale = (con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val itemView = inflater.inflate(R.layout.card_image, parent, false)
        return FavViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        if (favs != null) {
            val current = favs!![position]
            holder.bindTo(current)
            holder.itemView.apply {
                isLongClickable = true
                isClickable = true
            }
            holder.itemView.setOnClickListener {
                //launch wall activity
                val wallIntent = Intent(con, WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.WALL_URL, current.favUrl)
                    putExtra(WallActivity.GIF, current.isGif)
                    putExtra(WallActivity.INDEX, position)
                    putExtra(WallActivity.FROM_MAIN, false)
                    putExtra(WallActivity.LIST, WallActivity.listToJson(null, favs))
                }
                con.startActivity(wallIntent)
            }
        }
    }

    fun setFavs(favs: List<FavImage>) {
        this.favs = favs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (favs != null)
            favs!!.size
        else
            0
    }

    fun getFavAtPosition(position: Int): FavImage {
        return favs!![position]
    }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img: ImageView = itemView.findViewById(R.id.image_holder)

        fun bindTo(saved: FavImage) {
            val url = saved.favUrl
            if (saved.isGif) {
                Glide.with(con).asGif().load(url).override(width / scale, height / 4).centerCrop().into(img)
            } else {
                Glide.with(con).load(url).override(width / 2, height / 4).centerCrop().into(img)
            }
        }
    }
}
