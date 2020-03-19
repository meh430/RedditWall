package com.mehul.redditwall.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leinardi.android.speeddial.SpeedDialView
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.FavAdapter
import com.mehul.redditwall.favorites.FavImage
import com.mehul.redditwall.favorites.FavViewModel
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.ProgressNotify
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutionException

class FavImageActivity : AppCompatActivity() {
    private var adapter: FavAdapter? = null
    private var favViewModel: FavViewModel? = null
    private var loading: ProgressBar? = null
    private var favImages: List<FavImage?>? = ArrayList()
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var favJob: Job? = null
    private var width = 1080
    private var height = 1920
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_image)
        supportActionBar?.elevation = 0F
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
        favViewModel!!.allFav?.observe(this, Observer { favs ->
            favImages = favs
            loading?.visibility = View.VISIBLE
            favJob = uiScope.launch {
                loadFavBits(favs, con)
            }
        })

        val speedView = findViewById<SpeedDialView>(R.id.speedDial)
        speedView.inflate(R.menu.fab_menu)
        speedView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.delete_all -> {
                    val confirmDelete = AlertDialog.Builder(this)
                    confirmDelete.setTitle("Are you sure?")
                    confirmDelete.setMessage("Do you want to clear your favorites?")
                    confirmDelete.setPositiveButton("Yes") { _, _ ->
                        favViewModel!!.deleteAll()
                        Toast.makeText(this@FavImageActivity, "Deleted favorite images", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.setNegativeButton("No") { _, _ ->
                        Toast.makeText(this@FavImageActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.show()
                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.down_all -> {
                    //Toast.makeText(con, "TODO", Toast.LENGTH_SHORT).show()
                    if (ContextCompat.checkSelfPermission(getCon(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WallActivity.WRITE)
                    } else {
                        uiScope.launch {
                            downloadAllImages()
                        }
                    }

                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.random -> {
                    if (favImages!!.isEmpty()) {
                        Toast.makeText(con, "No items", Toast.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }

                    var randomNum = (0..favImages!!.size).random()
                    while (randomNum >= favImages!!.size || randomNum < 0) {
                        randomNum = (0..favImages!!.size).random()
                    }
                    if (randomNum == favImages!!.size) randomNum = favImages!!.size - 1
                    val current = adapter!!.getFavAtPosition(randomNum)

                    val wallIntent = Intent(con, WallActivity::class.java)
                    wallIntent.apply {
                        putExtra(WallActivity.WALL_URL, current.url)
                        putExtra(WallActivity.GIF, current.hasGif())
                        putExtra(WallActivity.INDEX, randomNum)
                        putExtra(WallActivity.FROM_FAV, true)
                        putExtra(WallActivity.LIST, WallActivity.listToJson(adapter!!.getFavs()))
                        putExtra(WallActivity.FAV_LIST, favImages!![randomNum]?.favName)
                    }
                    con.startActivity(wallIntent)
                    return@OnActionSelectedListener false
                }
            }

            false
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun loadFavBits(favs: List<FavImage?>?, con: Context) {
        val bits = ArrayList<BitURL>()

        withContext(Dispatchers.Default) {
            val displayMetrics = DisplayMetrics()
            (con as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels
            val scale = (con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                    .getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2

            for (fav in favs!!) {
                if (!isActive) {
                    break
                }

                var bitmap: Bitmap? = null
                withContext(Dispatchers.IO) {
                    if (!fav!!.isGif) {
                        try {
                            bitmap = Glide.with(con).asBitmap().load(fav.favUrl)
                                    .override(width / scale, height / 4).centerCrop().submit().get()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: ExecutionException) {
                            e.printStackTrace()
                        }
                    }
                }
                val temp = BitURL(bitmap, fav!!.favUrl, fav.postLink)
                Log.e("BITMAP", bitmap.toString())
                temp.setGif(fav.isGif)
                withContext(Dispatchers.Main) {
                    bits.add(temp)
                    adapter!!.setFavs(bits, favs)
                    loading?.visibility = View.GONE
                }
            }

            withContext(Dispatchers.Main) {
                loading?.visibility = View.GONE
                Log.e("BITS", bits.toString())
                adapter!!.setFavs(bits, favs)
                findViewById<View>(R.id.fav_empty).visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == WallActivity.WRITE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uiScope.launch {
                    downloadAllImages()
                }
            } else {
                Toast.makeText(this, "Cannot download, please grant permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun downloadAllImages() {
        val downloadOriginal = getCon()
                .getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)
        val notify = ProgressNotify(getCon(), favImages!!.size)
        var finalName = ""
        notify.sendNotification()
        width = getCon()
                .getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getInt(SettingsActivity.IMG_WIDTH, width)
        height = getCon()
                .getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getInt(SettingsActivity.IMG_HEIGHT, height)

        withContext(Dispatchers.IO) {
            for (i in favImages!!.indices) {
                val bitmap = if (downloadOriginal) {
                    Glide.with(getCon()).asBitmap().load(favImages!![i]?.favUrl).submit().get()
                } else {
                    Glide.with(getCon()).asBitmap().load(favImages!![i]?.favUrl).override(width, height).submit().get()
                }

                val root = Environment.getExternalStorageDirectory().toString()
                val myDir = File("$root/RedditWalls")
                myDir.mkdirs()
                val fname = (0..999999999).random().toString().replace(" ", "") + ".jpg"
                finalName = fname
                val file = File(myDir, fname)
                if (file.exists())
                    file.delete()
                try {
                    val out = FileOutputStream(file)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                    MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)
                    withContext(Dispatchers.Main) {
                        Log.e("PROGRESS", "$i / ${favImages!!.size}")
                        notify.updateProgress(i)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        notify.finish(finalName)
    }

    private fun getCon(): Context {
        return this
    }

    override fun onResume() {
        super.onResume()
        if (adapter?.itemCount != favImages?.size) {
            favJob = uiScope.launch {
                loadFavBits(favImages, getCon())
            }
        }
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