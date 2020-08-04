package com.mehul.redditwall.adapters

import android.content.Context
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
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.objects.BitURL


class ImageAdapter internal constructor(private val context: Context,
                                        private var images: ArrayList<BitURL>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val inflater: LayoutInflater
    private val width: Int
    private val height: Int
    private val scale: Int

    init {
        val dims = AppUtils.getDimensions(context)
        width = dims[0]
        height = dims[1]
        scale = AppUtils.getGridImageScale(context)
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = inflater.inflate(R.layout.card_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = images[position]
        /*
        if (current.hasGif()) {
            Glide.with(context).asGif().load(current.url).override(width / scale, height / 4).into(holder.image)
        } else {
            try {
                if (current.img == null || (current.img != null && (current.img as Bitmap).isRecycled)) {
                    Glide.with(context).load(current.url).placeholder(ColorDrawable(Color.GRAY))
                            .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop().into(holder.image)
                    if (holder.image.drawable != null && holder.image.drawable !is ColorDrawable) {
                        Log.e("SAVED", "saved image $position")
                        images[position].img = (holder.image.drawable as BitmapDrawable).bitmap
                    }
                } else {
                    holder.image.setImageBitmap(current.img)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FAIL", "Recycler error")
                Glide.with(context).load(current.url).placeholder(ColorDrawable(Color.GRAY))
                        .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop().into(holder.image)
                if (holder.image.drawable != null && holder.image.drawable !is ColorDrawable) {
                    Log.e("SAVED", "saved image $position")
                    images[position].img = (holder.image.drawable as BitmapDrawable).bitmap
                }
            }
        }*/

        val temp = ContextCompat.getDrawable(context, R.drawable.ic_android)
        val errorDraw = ScaleDrawable(temp, 0, (width / scale).toFloat(), (height / 4).toFloat())
        if (current.hasGif()) {
            Glide.with(context).asGif().load(current.url)
                    .override(width / scale, height / 4).centerCrop().into(holder.image)
        } else {
            if (current.img == null) {
                val requestOptions = RequestOptions()
                requestOptions.apply {
                    diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    centerCrop()
                    dontAnimate()
                    dontTransform()
                    placeholder(errorDraw)
                    error(errorDraw)
                    override(width / scale, height / 4)
                }

                Glide.with(context).applyDefaultRequestOptions(requestOptions).load(current.url).into(holder.image)
            } else {
                holder.image.setImageBitmap(current.img)
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setList(list: ArrayList<BitURL>) {
        images = list
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_holder)
    }
}
