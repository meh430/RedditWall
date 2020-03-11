package com.mehul.redditwall

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehul.redditwall.favorites.FavAdapter
import com.mehul.redditwall.favorites.FavImage
import com.mehul.redditwall.favorites.FavViewModel
import kotlinx.coroutines.*
import java.util.concurrent.ExecutionException

class FavImageActivity : AppCompatActivity() {
    private var adapter: FavAdapter? = null
    private var favViewModel: FavViewModel? = null
    private var loading: ProgressBar? = null
    private var favImages: List<FavImage>? = null
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var favJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_image)
        loading = findViewById(R.id.fav_loading)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        val recycler = findViewById<RecyclerView>(R.id.fav_scroll)
        adapter = FavAdapter(this, ArrayList())
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 2)
        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val saved = favImages?.get(position)
                        favViewModel!!.deleteFavImage(saved)
                        adapter!!.notifyDataSetChanged()
                    }
                })
        helper.attachToRecyclerView(recycler)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        val con = this
        favViewModel!!.allFav.observe(this, Observer { favs ->
            loading?.visibility = View.VISIBLE
            favJob = uiScope.launch {
                loadFavBits(favs, con)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        } else if (item.itemId == R.id.clear_list) {
            val confirmSubs = AlertDialog.Builder(this)
            confirmSubs.setTitle("Are you sure?")
            confirmSubs.setMessage("Do you want to clear your favorites?")
            confirmSubs.setPositiveButton("Yes") { _, _ ->
                favViewModel!!.deleteAll()
                Toast.makeText(this@FavImageActivity, "Deleted favorite images", Toast.LENGTH_SHORT).show()
            }
            confirmSubs.setNegativeButton("No") { _, _ -> Toast.makeText(this@FavImageActivity, "Cancelled", Toast.LENGTH_SHORT).show() }
            confirmSubs.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun loadFavBits(favs: List<FavImage>, con: Context) {
        val bits = ArrayList<BitURL>()

        withContext(Dispatchers.Default) {
            val displayMetrics = DisplayMetrics()
            (con as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            val scale = (con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                    .getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2

            for (fav in favs) {
                if (!isActive) {
                    break
                }

                var bitmap: Bitmap? = null
                withContext(Dispatchers.IO) {
                    if (!fav.isGif) {
                        try {
                            bitmap = Glide.with(con).asBitmap().load(fav.favUrl).override(width / scale, height / 4).centerCrop().submit().get()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        }
                    }
                }
                val temp = BitURL(bitmap, fav.favUrl)
                Log.e("BITMAP", bitmap.toString())
                temp.setGif(fav.isGif)
                withContext(Dispatchers.Main) {
                    bits.add(temp)
                }
            }

            withContext(Dispatchers.Main) {
                loading?.visibility = View.GONE
                Log.e("BITS", bits.toString())
                adapter!!.setFavs(bits)
                findViewById<View>(R.id.fav_empty).visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return true
    }


    override fun onStop() {
        super.onStop()
        favJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        favJob?.cancel()
    }
}