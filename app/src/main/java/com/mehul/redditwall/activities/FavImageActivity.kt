package com.mehul.redditwall.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.leinardi.android.speeddial.SpeedDialView
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.FavAdapter
import com.mehul.redditwall.databinding.ActivityFavImageBinding
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.FavImage
import com.mehul.redditwall.objects.ProgressNotify
import com.mehul.redditwall.viewmodels.FavViewModel
import kotlinx.coroutines.*

class FavImageActivity : AppCompatActivity() {
    private lateinit var favAdapter: FavAdapter
    private lateinit var favViewModel: FavViewModel
    private var favImages: List<FavImage> = ArrayList()
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var favJob: Job? = null
    private var firstLoad = true

    private lateinit var binding: ActivityFavImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        favAdapter = FavAdapter(this, ArrayList())
        val recycler = binding.favRecycler.apply {
            adapter = favAdapter
            layoutManager = GridLayoutManager(getCon(), 2)
        }
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
                        val imgLoading = favJob?.isActive ?: false
                        if (!imgLoading) {
                            val swipedImage = favImages[position]
                            uiScope.launch {
                                deleteImage(swipedImage)
                            }
                            favViewModel.deleteFavImage(swipedImage)
                            favAdapter.notifyDataSetChanged()
                        } else {
                            Snackbar.make(binding.root, "Please wait...", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                })
        helper.attachToRecyclerView(recycler)
        favViewModel.allFav.observe(this, Observer { favs ->
            favImages = favs
            if (firstLoad) {
                firstLoad = false
                favJob = uiScope.launch {
                    loadFavBitmaps(favs, getCon())
                }
            } else {
                favAdapter.setFavs(favs)
            }

        })

        val speedView = binding.speedDial
        speedView.inflate(R.menu.fab_menu)
        speedView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.delete_all -> {
                    val confirmDelete =
                            MaterialAlertDialogBuilder(getCon(), R.style.MyThemeOverlayAlertDialog).apply {
                                setTitle("Are You Sure?")
                                setMessage("Do you want to clear ${favImages.size} favorite images?")
                                setPositiveButton("Yes") { _, _ ->
                                    favViewModel.deleteAll()
                                    Snackbar.make(binding.root, "Deleted favorite images", Snackbar.LENGTH_SHORT).show()
                                }
                                setNegativeButton("No") { _, _ ->
                                    Snackbar.make(binding.root, "Cancelled", Snackbar.LENGTH_SHORT).show()
                                }
                            }
                    confirmDelete.show()
                    return@OnActionSelectedListener false
                }
                R.id.down_all -> {
                    if (ContextCompat.checkSelfPermission(getCon(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WallActivity.WRITE)
                    } else {
                        if (favImages.isEmpty()) {
                            Snackbar.make(binding.root, "No items", Snackbar.LENGTH_SHORT).show()
                            return@OnActionSelectedListener false
                        }

                        uiScope.launch {
                            downloadAllImages()
                        }
                    }

                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.random -> {
                    if (favImages.isEmpty()) {
                        Snackbar.make(binding.root, "No items", Snackbar.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }

                    var randomIndex = (0..favImages.size).random()
                    while (randomIndex >= favImages.size || randomIndex < 0) {
                        randomIndex = (0..favImages.size).random()
                    }
                    if (randomIndex == favImages.size) randomIndex = favImages.size - 1
                    val selectedImage = favImages[randomIndex]
                    val wallIntent = Intent(getCon(), WallActivity::class.java)
                    val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                    val jsonList = gson.toJson(favImages)
                    wallIntent.apply {
                        putExtra(WallActivity.WALL_URL, selectedImage.favUrl)
                        putExtra(WallActivity.GIF, selectedImage.isGif)
                        putExtra(WallActivity.INDEX, randomIndex)
                        putExtra(PostActivity.POST_LINK, selectedImage.postLink)
                        putExtra(WallActivity.FROM_FAV, true)
                        putExtra(WallActivity.LIST, jsonList)
                        putExtra(WallActivity.FAV_LIST, selectedImage.favName)
                    }
                    getCon().startActivity(wallIntent)
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

    private suspend fun deleteImage(image: FavImage) {
        binding.remaining.visibility = View.VISIBLE
        val favBitmaps = favAdapter.getBitList()
        withContext(Dispatchers.Default) {
            val url = image.favUrl
            for (i in 0..(favBitmaps.size)) {
                if (url == favBitmaps[i].url) {
                    favBitmaps.removeAt(i)
                    break
                }
            }

            withContext(Dispatchers.Main) {
                favAdapter.setFavs(favBitmaps, favImages)
                binding.remaining.visibility = View.GONE
            }
        }
    }

    private suspend fun loadFavBitmaps(favs: List<FavImage>, con: Context) {
        binding.favLoading.visibility = View.VISIBLE
        withContext(Dispatchers.Default) {
            val favBitmaps = ArrayList<BitURL>()
            for (i in favs.indices) {
                val fav = favs[i]
                if (!isActive) {
                    break
                }

                var bitmap: Bitmap? = null
                /*withContext(Dispatchers.IO) {
                    try {
                        if (!fav.isGif && i % 4 == 0) {
                            bitmap = AppUtils.getGridImageBitmap(con, fav.favUrl)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        bitmap = null
                    }
                }*/

                val currLoadedImage = BitURL(bitmap, fav.favUrl, fav.postLink)
                currLoadedImage.setGif(fav.isGif)
                favBitmaps.add(currLoadedImage)

                withContext(Dispatchers.Main) {
                    if (i == 0) {
                        binding.remaining.visibility = View.VISIBLE
                    } else if (i % 4 == 0) {
                        favAdapter.setFavs(favBitmaps, favs)
                        binding.favLoading.visibility = View.GONE
                    }
                }
            }

            withContext(Dispatchers.Main) {
                favAdapter.setFavs(favBitmaps, favs)
                binding.remaining.visibility = View.GONE
            }
        }

        binding.favLoading.visibility = View.GONE
        binding.favEmpty.visibility = if (favAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == WallActivity.WRITE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uiScope.launch {
                    downloadAllImages()
                }
            } else {
                Snackbar.make(binding.root, "Cannot download, please grant permissions", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun downloadAllImages() {
        val downloadOriginal = AppUtils.getPreferences(getCon()).getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)
        val notify = ProgressNotify(getCon(), favImages.size)
        notify.sendNotification()
        val downloadDimensions = AppUtils.getWallDimensions(getCon())

        withContext(Dispatchers.IO) {
            for (i in favImages.indices) {
                val bitmap = if (downloadOriginal) {
                    Glide.with(getCon()).asBitmap().load(favImages[i].favUrl).submit().get()
                } else {
                    Glide.with(getCon()).asBitmap().load(favImages[i].favUrl).override(downloadDimensions[0], downloadDimensions[1]).submit().get()
                }
                AppUtils.saveBitmap(bitmap, getCon())
                withContext(Dispatchers.Main) {
                    Log.e("PROGRESS", "$i / ${favImages.size}")
                    notify.updateProgress(i)
                }
            }
        }
        notify.finish()
    }

    private fun getCon(): Context {
        return this
    }

    override fun onResume() {
        super.onResume()
        if (favAdapter.itemCount != favImages.size && !favJob!!.isActive) {
            favJob = uiScope.launch {
                loadFavBitmaps(favImages, getCon())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favJob?.cancel()
    }
}