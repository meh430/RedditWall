package com.mehul.redditwall.rv_adapters

import android.graphics.drawable.ScaleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mehul.redditwall.R
import com.mehul.redditwall.objects.WallImage

class WallImageAdapter(private val dimensions: IntArray, private val lowRes: Boolean) :
        RecyclerView.Adapter<WallImageAdapter.WallImageViewHolder>() {
    var wallImages = ArrayList<WallImage>()
    private val width: Int = dimensions[0]
    private val height: Int = dimensions[1]

    fun updateWallImages(updatedList: ArrayList<WallImage>) {
        wallImages = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_image, parent, false)
        return WallImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WallImageViewHolder, position: Int) {
        val currentImage = wallImages[position]
        val placeholder = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_android)
        val errorDraw = ScaleDrawable(placeholder, 0, (width / 2).toFloat(), (height / 4).toFloat())
        val requestOptions = RequestOptions()
        requestOptions.apply {
            diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            centerCrop()
            dontAnimate()
            dontTransform()
            placeholder(errorDraw)
            error(errorDraw)
        }

        Glide.with(holder.image.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(if (lowRes) currentImage.previewUrl else currentImage.imgUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return wallImages.size
    }

    fun getWallImage(position: Int): WallImage {
        return if (position >= wallImages.size) {
            wallImages[wallImages.size - 1]
        } else {
            wallImages[position]
        }
    }

    inner class WallImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_holder)
    }
}