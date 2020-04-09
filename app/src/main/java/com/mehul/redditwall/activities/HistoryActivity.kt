package com.mehul.redditwall.activities

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.leinardi.android.speeddial.SpeedDialView
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.HistAdapter
import com.mehul.redditwall.history.HistViewModel
import com.mehul.redditwall.history.HistoryItem
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.ProgressNotify
import com.mehul.redditwall.objects.RecyclerListener
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class HistoryActivity : AppCompatActivity() {
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var currSort = R.id.recent
    private var json = ""
    private var histJob: Job? = null
    private var adapt: HistAdapter? = null
    private var histViewModel: HistViewModel? = null
    private var histories: List<HistoryItem?> = ArrayList()
    private var loading: ProgressBar? = null
    private var width = 1080
    private var height = 1920

    private fun sortList(sort: Int, list: List<HistoryItem?>): List<HistoryItem?> {
        return when (sort) {
            R.id.alpha -> {
                list.sortedWith(compareBy { it?.subName }).asReversed()
            }
            R.id.oldest -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it!!.internalDate) })

            }
            else -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it!!.internalDate) }).asReversed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val sortIcon: Drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_sort)!!
        val prefs = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        val dark = prefs.getBoolean(SettingsActivity.DARK, false)
        sortIcon.setTint(if (dark) Color.WHITE else Color.BLACK)
        width = prefs.getInt(SettingsActivity.IMG_WIDTH, width)
        height = prefs.getInt(SettingsActivity.IMG_HEIGHT, height)
        toolbar.overflowIcon = sortIcon
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        loading = findViewById(R.id.hist_loading)
        histViewModel = ViewModelProvider(this@HistoryActivity).get(HistViewModel(application)::class.java)
        adapt = HistAdapter(this)
        val recycler = findViewById<RecyclerView>(R.id.hist_scroll).apply {
            adapter = adapt
            layoutManager = LinearLayoutManager(getCon())
        }
        adapt!!.setHistories(histories)
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
                        val saved = adapt!!.getHistory(position)
                        histViewModel!!.deleteHist(saved)
                        adapt!!.notifyDataSetChanged()
                    }
                })
        helper.attachToRecyclerView(recycler)
        histViewModel!!.allHist!!.observe(this, Observer { hists ->
            this.histories = sortList(currSort, hists!!)
            adapt!!.setHistories(histories)
            uiScope.launch {
                json = convertToJSON(histories)
            }
            findViewById<View>(R.id.hist_empty).visibility = if (adapt!!.itemCount == 0) View.VISIBLE else View.GONE
        })
        recycler.addOnItemTouchListener(RecyclerListener(this, recycler, object : RecyclerListener.OnItemClickListener {
            override fun onLongItemClick(view: View?, position: Int) {
                cancelThreads()
                val launchMain = Intent(getCon(), MainActivity::class.java)
                launchMain.apply {
                    putExtra(MainActivity.SAVED, histories[position]?.subName)
                    putExtra(MainActivity.OVERRIDE, true)
                }
                val clipboard: ClipboardManager? = getCon().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", histories[position]?.subName)
                assert(clipboard != null)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(getCon(), "Saved to clipboard", Toast.LENGTH_SHORT).show()
                getCon().startActivity(launchMain)
            }

            override fun onItemClick(view: View, position: Int) {
                cancelThreads()
                val current = histories[position]
                val wallIntent = Intent(getCon(), WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.WALL_URL, current?.url)
                    putExtra(WallActivity.GIF, false)
                    putExtra(WallActivity.FROM_HIST, true)
                    putExtra(WallActivity.FAV_LIST, current?.subName)
                    putExtra(WallActivity.INDEX, position)
                    putExtra(WallActivity.LIST, json)
                }
                Log.e("JSON", json)

                getCon().startActivity(wallIntent)
            }
        }))

        val speedView = findViewById<SpeedDialView>(R.id.speedDial)
        speedView.inflate(R.menu.fab_menu)
        speedView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.delete_all -> {
                    val confirmDelete =
                            MaterialAlertDialogBuilder(getCon(), R.style.MyThemeOverlayAlertDialog).apply {
                                setTitle("Are You Sure?")
                                setMessage("Do you want to clear ${histories.size} history items?")
                                setPositiveButton("Yes") { _, _ ->
                                    histViewModel!!.deleteAll()
                                    Toast.makeText(this@HistoryActivity, "Cleared history", Toast.LENGTH_SHORT).show()
                                }
                                setNegativeButton("No") { _, _ ->
                                    Toast.makeText(this@HistoryActivity, "Cancelled", Toast.LENGTH_SHORT).show()
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
                        if (histories.isEmpty()) {
                            Toast.makeText(getCon(), "No items", Toast.LENGTH_SHORT).show()
                            return@OnActionSelectedListener false
                        }
                        uiScope.launch {
                            downloadAllImages()
                        }
                    }
                    return@OnActionSelectedListener false
                }
                R.id.random -> {
                    if (histories.isEmpty()) {
                        Toast.makeText(getCon(), "No items", Toast.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }


                    var randomNum = (0..histories.size).random()
                    while (randomNum >= histories.size || randomNum < 0) {
                        randomNum = (0..histories.size).random()
                    }
                    cancelThreads()
                    val current = histories[randomNum]
                    val wallIntent = Intent(getCon(), WallActivity::class.java)
                    wallIntent.apply {
                        putExtra(WallActivity.WALL_URL, current?.url)
                        putExtra(WallActivity.GIF, false)
                        putExtra(WallActivity.FROM_FAV, false)
                        putExtra(MainActivity.QUERY, current?.subName)
                    }
                    wallIntent.putExtra(WallActivity.INDEX, randomNum)
                    wallIntent.putExtra(WallActivity.LIST, json)
                    Log.e("JSON", json)

                    getCon().startActivity(wallIntent)
                    return@OnActionSelectedListener false
                }
            }

            false
        })
    }

    private fun cancelThreads() {
        histJob?.cancel()
    }

    override fun onStop() {
        super.onStop()
        cancelThreads()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelThreads()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currSort = item.itemId
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }

            else -> {
                histories = sortList(currSort, histories)
            }
        }

        if (item.itemId != android.R.id.home) {
            adapt?.setHistories(histories)
            uiScope.launch {
                json = convertToJSON(histories)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
        val notify = ProgressNotify(getCon(), histories.size)
        notify.sendNotification()
        var finalName = ""

        withContext(Dispatchers.IO) {
            val done = ArrayList<String?>()
            for (i in histories.indices) {
                if (done.contains(histories[i]?.url)) {
                    continue
                }
                val bitmap = if (downloadOriginal) {
                    Glide.with(getCon()).asBitmap().load(histories[i]?.url).submit().get()
                } else {
                    Glide.with(getCon()).asBitmap().load(histories[i]?.url).override(width, height).submit().get()
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
                    done.add(histories[i]?.url)
                    withContext(Dispatchers.Main) {
                        Log.e("PROGRESS", "$i / ${histories.size}")
                        notify.updateProgress(i)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        notify.finish(finalName)
    }

    private suspend fun convertToJSON(hists: List<HistoryItem?>): String {
        var json = ""
        withContext(Dispatchers.Default) {
            val bits = ArrayList<BitURL>()
            for (i in hists) {
                val temp = BitURL(null, i!!.url, i.postLink)
                temp.setGif(false)
                bits.add(temp)
            }
            json = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(bits)
        }
        return json
    }

    private fun getCon(): Context {
        return this
    }
}
