package com.mehul.redditwall.adapters


import android.content.Context
import android.content.Intent
import android.graphics.drawable.ScaleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.GsonBuilder
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.PostActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.activities.WallActivity
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.FavImage

class FavAdapter(private val con: Context, lis: ArrayList<BitURL>) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(con)
    private var favList: List<FavImage?> = ArrayList()
    private var favs = lis
    private val width: Int
    private val height: Int
    private val scale: Int

    init {
        val dims = MainActivity.getDimensions(con)
        width = dims[0]
        height = dims[1]
        scale = (con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val itemView = inflater.inflate(R.layout.card_image, parent, false)
        return FavViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val current = favs[position]
        holder.bindTo(current, position)
        holder.itemView.apply {
            isLongClickable = true
            isClickable = true
        }
        holder.itemView.setOnClickListener {
            val wallIntent = Intent(con, WallActivity::class.java)
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            val jsonList = gson.toJson(favList)
            wallIntent.apply {
                putExtra(PostActivity.POST_LINK, current.postLink)
                putExtra(WallActivity.WALL_URL, current.url)
                putExtra(WallActivity.GIF, current.hasGif())
                putExtra(WallActivity.INDEX, position)
                putExtra(WallActivity.FROM_FAV, true)
                putExtra(WallActivity.LIST, jsonList)
                putExtra(WallActivity.FAV_LIST, favList[position]?.favName)
            }
            con.startActivity(wallIntent)
        }
    }

    fun setFavs(favs: ArrayList<BitURL>, favLists: List<FavImage?>) {
        this.favs = favs
        this.favList = favLists
        notifyDataSetChanged()
    }

    fun setFavs(favLists: List<FavImage?>) {
        this.favList = favLists
        notifyDataSetChanged()
    }

    fun getBitList(): ArrayList<BitURL> {
        return this.favs
    }

    override fun getItemCount(): Int {
        return favs.size
    }

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img: ImageView = itemView.findViewById(R.id.image_holder)

        fun bindTo(saved: BitURL, p: Int) {
            val url = saved.url
            /*if (saved.hasGif()) {
                Glide.with(con).asGif().load(url).override(width / scale, height / 4).into(img)
            } else {
                try {
                    if (saved.img == null || (saved.img != null && (saved.img as Bitmap).isRecycled)) {
                        Glide.with(con).load(url).placeholder(ColorDrawable(Color.GRAY))
                                .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop().into(img)
                        if (img.drawable != null && img.drawable !is ColorDrawable) {
                            Log.e("SAVED", "saved image $p")
                            favs[p].img = (img.drawable as BitmapDrawable).bitmap
                        }
                    } else {
                        img.setImageBitmap(saved.img)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("FAIL", "Recycler error")
                    Glide.with(con).load(url).placeholder(ColorDrawable(Color.GRAY))
                            .override(width / scale, height / 4).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop().into(img)
                    if (img.drawable != null && img.drawable !is ColorDrawable) {
                        Log.e("SAVED", "saved image $p")
                        favs[p].img = (img.drawable as BitmapDrawable).bitmap
                    }
                }
            }*/
            val temp = ContextCompat.getDrawable(con, R.drawable.ic_android)
            val errorDraw = ScaleDrawable(temp, 0, (width / scale).toFloat(), (height / 4).toFloat())
            if (saved.hasGif()) {
                Glide.with(con).asGif().load(url).override(width / scale, height / 4).centerCrop().into(img)
            } else {
                if (saved.img == null) {
                    Glide.with(con).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).error(errorDraw).placeholder(errorDraw)
                            .override(width / scale, height / 4).centerCrop().into(img)
                } else {
                    img.setImageBitmap(saved.img)
                }
            }
        }
    }
}
