package com.mehul.redditwall

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter internal constructor(private val context: Context,
                                        private var images: ArrayList<BitURL>?) :
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
