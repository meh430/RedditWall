package com.mehul.redditwall

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*

class ImageAdapter internal constructor(private val context: Context,
                                        private var images: ArrayList<BitURL>?,
                                        private val task1: AsyncTask<String, Void, Void>?,
                                        private val task2: AsyncTask<String, Void, Void>?) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val inflater: LayoutInflater
    private val width: Int
    private val height: Int

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = inflater.inflate(R.layout.card_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (images != null) {
            val current = images!![position]
            if (current.img == null) {
                Glide.with(context).asGif().load(current.url).override(width / 2, height / 4).into(holder.image)
            } else {
                holder.image.setImageBitmap(current.img)
            }
            holder.itemView.setOnClickListener {
                task1?.cancel(true)

                task2?.cancel(true)
                val wallIntent = Intent(context, WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.INDEX, position)
                    putExtra(WallActivity.WALL_URL, current.url)
                    putExtra(WallActivity.GIF, current.img == null)
                    putExtra(WallActivity.FROM_MAIN, true)
                }
                val prevs = ArrayList<BitURL>()
                for (i in (if (position >= 10) position - 10 else 0) until images!!.size) {
                    prevs.add(images!![i])
                }
                wallIntent.putExtra(WallActivity.LIST, WallActivity.listToJson(prevs, null))

                context.startActivity(wallIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return images!!.size
    }

    fun setList(list: ArrayList<BitURL>) {
        images = list
        notifyDataSetChanged()
    }


    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_holder)
    }
}
