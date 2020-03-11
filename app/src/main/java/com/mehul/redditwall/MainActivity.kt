@file:Suppress("DEPRECATION")

package com.mehul.redditwall

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import kotlinx.coroutines.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var queryString = ""
    private var defaultLoad = ""
    private var search: EditText? = null
    private var hotImages = ArrayList<BitURL>()
    private var topImages = ArrayList<BitURL>()
    private var newImages = ArrayList<BitURL>()
    private var adapter: ImageAdapter? = null
    private var loading: ProgressBar? = null
    private var bottomLoading: ProgressBar? = null
    private var info: TextView? = null
    private var imageJob: Job? = null
    private var scrollJob: Job? = null
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var hotChip: Chip? = null
    private var newChip: Chip? = null
    private var topChip: Chip? = null
    private var currentSort: Int = 0
    private var preferences: SharedPreferences? = null
    private var currCon: Context? = null

    @InternalCoroutinesApi
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        hotChip = findViewById(R.id.hot_chip)
        newChip = findViewById(R.id.new_chip)
        topChip = findViewById(R.id.top_chip)
        loading = findViewById(R.id.loading)
        info = findViewById(R.id.info)
        bottomLoading = findViewById(R.id.progressBar)
        search = findViewById(R.id.search)
        val imageScroll = findViewById<RecyclerView>(R.id.imageScroll)
        imageScroll.layoutManager = GridLayoutManager(this, 2)
        currCon = this
        imageScroll.addOnItemTouchListener(RecyclerListener(this, imageScroll, object : RecyclerListener.OnItemClickListener {
            override fun onLongItemClick(view: View?, position: Int) {}

            override fun onItemClick(view: View, position: Int) {
                var p = position
                val currList: ArrayList<BitURL>? = when (currentSort) {
                    HOT -> hotImages
                    NEW -> newImages
                    else -> topImages
                }
                cancelThreads()
                p = if (p <= 0) 0 else p
                val current = currList!![p]
                val wallIntent = Intent(currCon, WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.WALL_URL, current.getUrl())
                    putExtra(WallActivity.GIF, current.getImg() == null)
                    putExtra(WallActivity.FROM_MAIN, true)
                }
                val prevs = ArrayList<BitURL>()
                for (i in (if (p >= 10) p - 10 else 0) until currList.size) {
                    prevs.add(currList[i])
                }

                val index = if (p >= 10) 10 else p
                wallIntent.putExtra(WallActivity.INDEX, index)
                wallIntent.putExtra(WallActivity.LIST, Gson().toJson(prevs))

                currCon!!.startActivity(wallIntent)
            }
        }))
        hotImages = ArrayList()
        newImages = ArrayList()
        topImages = ArrayList()
        preferences = getSharedPreferences(SharedPrefFile, Context.MODE_PRIVATE)
        defaultLoad = preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper").toString()

        when (preferences!!.getInt(SettingsActivity.SORT_METHOD, HOT)) {
            HOT -> {
                adapter = ImageAdapter(this, hotImages)
                currentSort = HOT
                hotChip!!.setChipBackgroundColorResource(R.color.chip)
                hotChip!!.setTextColor(Color.WHITE)
            }
            NEW -> {
                adapter = ImageAdapter(this, newImages)
                currentSort = NEW
                newChip!!.setChipBackgroundColorResource(R.color.chip)
                newChip!!.setTextColor(Color.WHITE)
            }
            else -> {
                topImages = ArrayList()
                currentSort = TOP
                adapter = ImageAdapter(this, topImages)
                topChip!!.setChipBackgroundColorResource(R.color.chip)
                topChip!!.setTextColor(Color.WHITE)
            }
        }

        imageScroll.adapter = adapter


        hotChip!!.setOnClickListener(this)
        newChip!!.setOnClickListener(this)
        topChip!!.setOnClickListener(this)

        val savedIntent = intent

        defaultLoad = (if (savedIntent.getBooleanExtra(OVERRIDE, false)) {
            savedIntent.getStringExtra(SAVED)
        } else {
            preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper")
        }).toString()

        search!!.hint = defaultLoad

        val connMgr: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        if (connMgr != null) {
            networkInfo = connMgr.activeNetworkInfo
        }
        preferences!!.edit().putString(QUERY, defaultLoad).apply()
        if (networkInfo != null && networkInfo.isConnected) {
            imageJob = uiScope.launch {
                loadImages(getCon(), defaultLoad, true, getList())
            }
        } else {
            info!!.visibility = View.VISIBLE
            info!!.text = "No Network"
        }

        imageScroll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    //down
                    if (imageJob != null && imageJob!!.isActive) {
                        return
                    }

                    if (scrollJob == null) {
                        cancelThreads()
                        scrollJob = uiScope.launch {
                            loadImages(getCon(), if (queryString.isEmpty()) defaultLoad else queryString, false, getList())
                        }
                    } else if (!scrollJob!!.isActive) {
                        cancelThreads()

                        scrollJob = uiScope.launch {
                            loadImages(getCon(), if (queryString.isEmpty()) defaultLoad else queryString, false, getList())
                        }
                    }
                }
            }
        })
    }

    private fun getList(): ArrayList<BitURL> = when (currentSort) {
        NEW -> newImages
        HOT -> hotImages
        else -> topImages
    }

    private fun getCon(): Context {
        return this
    }

    public override fun onStop() {
        super.onStop()
        Log.e("STOP", "STOP")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.e("DESTROY", "DESTROY")
        cancelThreads()
    }

    @InternalCoroutinesApi
    fun startSearch(view: View) {
        if (imageJob != null && imageJob!!.isActive) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show()
            return
        }
        cancelThreads()
        queryString = ""
        loading!!.visibility = View.VISIBLE
        info!!.visibility = View.INVISIBLE
        newImages.clear()
        hotImages.clear()
        topImages.clear()
        adapter!!.notifyDataSetChanged()
        queryString = search!!.text.toString()
        val inputManager: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager?.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)

        val connMgr: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        if (connMgr != null) {
            networkInfo = connMgr.activeNetworkInfo
        }

        if (networkInfo != null && networkInfo.isConnected && queryString.isNotEmpty()) {
            preferences!!.edit().putString(QUERY, queryString).apply()
            imageJob = uiScope.launch {
                loadImages(getCon(), queryString, true, getList())
            }

        } else {
            if (queryString.isEmpty()) {
                imageJob = uiScope.launch {
                    loadImages(getCon(), defaultLoad, true, getList())
                }

            } else {
                info!!.visibility = View.VISIBLE
                info!!.text = "No Network"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //Launch activities from menu here
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saved_subs -> {
                val launchSaved = Intent(this, SavedActivity::class.java)
                startActivity(launchSaved)
                return true
            }
            R.id.settings -> {
                val launchSettings = Intent(this, SettingsActivity::class.java)
                startActivity(launchSettings)
                return true
            }
            R.id.fav_pics -> {
                val launchFav = Intent(this, FavImageActivity::class.java)
                startActivity(launchFav)
                return true
            }
            R.id.recc_subs -> {
                val launchRec = Intent(this, RecActivity::class.java)
                startActivity(launchRec)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelThreads() {

        if (imageJob != null) {
            loading!!.visibility = View.GONE
            imageJob!!.cancel()
        }

        if (scrollJob != null) {
            bottomLoading!!.visibility = View.GONE
            scrollJob!!.cancel()
        }
    }

    @InternalCoroutinesApi
    private fun runQuery() {
        imageJob = uiScope.launch {
            loadImages(getCon(), if (queryString.isEmpty())
                defaultLoad
            else
                queryString, true, getList())
        }

        Log.e("BRUH", "$queryString, $defaultLoad")
    }

    @InternalCoroutinesApi
    override fun onClick(view: View) {
        val temp: String? = when (currentSort) {
            NEW -> AFTER_NEW
            HOT -> AFTER_HOT
            TOP -> AFTER_TOP
            else -> null
        }
        if (imageJob != null && imageJob!!.isActive && temp == null) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageJob != null && imageJob!!.isActive || scrollJob != null && scrollJob!!.isActive) {
            cancelThreads()
        }

        adapter!!.notifyDataSetChanged()
        val prefEdit = preferences!!.edit()
        hotChip!!.setTextColor(Color.BLACK)
        newChip!!.setTextColor(Color.BLACK)
        topChip!!.setTextColor(Color.BLACK)
        if (view == hotChip) {
            currentSort = HOT
            Log.e("CLICk", "CLICKED HOT")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, HOT)
            hotChip!!.setChipBackgroundColorResource(R.color.chip)
            hotChip!!.setTextColor(Color.WHITE)
            newChip!!.setChipBackgroundColorResource(R.color.white)
            topChip!!.setChipBackgroundColorResource(R.color.white)
            prefEdit.apply()
            if (hotImages.size > 0) {
                adapter!!.setList(hotImages)
            } else {
                adapter!!.setList(hotImages)
                runQuery()
            }
        } else if (view == newChip) {
            currentSort = NEW
            Log.e("CLICk", "CLICKED NEW")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, NEW)
            hotChip!!.setChipBackgroundColorResource(R.color.white)
            newChip!!.setChipBackgroundColorResource(R.color.chip)
            newChip!!.setTextColor(Color.WHITE)
            topChip!!.setChipBackgroundColorResource(R.color.white)
            prefEdit.apply()
            if (newImages.size > 0) {
                adapter!!.setList(newImages)
            } else {
                adapter!!.setList(newImages)
                runQuery()
            }
        } else if (view == topChip) {
            currentSort = TOP
            Log.e("CLICk", "CLICKED TOP")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, TOP)
            hotChip!!.setChipBackgroundColorResource(R.color.white)
            newChip!!.setChipBackgroundColorResource(R.color.white)
            topChip!!.setChipBackgroundColorResource(R.color.chip)
            topChip!!.setTextColor(Color.WHITE)
            prefEdit.apply()
            if (topImages.size > 0) {
                adapter!!.setList(topImages)
            } else {
                adapter!!.setList(topImages)
                runQuery()
            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun loadImages(con: Context, query: String, first: Boolean, images: ArrayList<BitURL>) {
        withContext(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                if (first) {
                    loading?.visibility = View.VISIBLE
                } else {
                    bottomLoading?.visibility = View.VISIBLE
                }
                info?.visibility = View.GONE
            }
            var loaded = false
            withContext(Dispatchers.IO) {
                val json = QueryRequest.getQueryJson(query, first, con)
                withContext(Dispatchers.Default) {
                    loaded = QueryRequest.loadImgsFromJSON(json, adapter, con, images, loading, first)
                }
            }

            withContext(Dispatchers.Main) {
                adapter?.notifyDataSetChanged()
                if (first) {
                    loading?.visibility = View.GONE
                } else {
                    bottomLoading?.visibility = View.GONE
                }

                if (first && !loaded) {
                    info?.visibility = View.VISIBLE
                    info?.text = "Subreddit does not exist or it has no images"
                }
            }
        }

    }

    companion object {
        const val SharedPrefFile = "com.mehul.redditwall"
        const val SAVED = "SAVED"
        const val OVERRIDE = "OVERRIDE"
        const val QUERY = "QUERY"
        const val NEW = 0
        const val HOT = 1
        const val TOP = 2
        var AFTER_NEW = ""
        var AFTER_HOT = ""
        var AFTER_TOP = ""
    }
}