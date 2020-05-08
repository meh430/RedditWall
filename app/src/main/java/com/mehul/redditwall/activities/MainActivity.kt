@file:Suppress("DEPRECATION")

package com.mehul.redditwall.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.ImageAdapter
import com.mehul.redditwall.databinding.ActivityMainBinding
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.RecyclerListener
import com.mehul.redditwall.rest.QueryRequest
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var queryString = ""
    private var defaultLoad = ""
    private var hotImages = ArrayList<BitURL>()
    private var topImages = ArrayList<BitURL>()
    private var newImages = ArrayList<BitURL>()
    private var adapter: ImageAdapter? = null
    private var imageJob: Job? = null
    private var scrollJob: Job? = null
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var currentSort: Int = 0
    private var infoShown = false
    private var infoPref = true
    private var preferences: SharedPreferences? = null

    private lateinit var binding: ActivityMainBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.infoTitle.paintFlags = binding.infoTitle.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        val imageScroll = binding.imageScroll
        imageScroll.layoutManager = GridLayoutManager(this, 2)
        imageScroll.addOnItemTouchListener(RecyclerListener(this, imageScroll, object : RecyclerListener.OnItemClickListener {
            override fun onLongItemClick(view: View?, position: Int) {}

            override fun onItemClick(view: View, position: Int) {
                var p = position
                val currList: ArrayList<BitURL>? = when (currentSort) {
                    HOT -> hotImages
                    NEW -> newImages
                    else -> topImages
                }
                val limit = currList?.size
                //cancelThreads()
                p = if (p <= 0) 0 else p
                val current = currList!![p]
                val wallIntent = Intent(getCon(), WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.WALL_URL, current.getUrl())
                    putExtra(WallActivity.GIF, current.hasGif())
                    putExtra(WallActivity.FROM_FAV, false)
                    putExtra(PostActivity.POST_LINK, current.postLink)
                    putExtra(QUERY, queryString)
                }
                val prevs = ArrayList<BitURL>()
                for (i in (if (p >= 10) p - 10 else 0) until limit!!) {
                    prevs.add(currList[i])
                }
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                val index = if (p >= 10) 10 else p
                val jsonList = gson.toJson(prevs)
                wallIntent.putExtra(WallActivity.INDEX, index)
                wallIntent.putExtra(WallActivity.LIST, jsonList)
                getCon().startActivity(wallIntent)
            }
        }))
        preferences = getSharedPreferences(SharedPrefFile, Context.MODE_PRIVATE)

        infoPref = preferences!!.getBoolean(SettingsActivity.SHOW_INFO, true)
        binding.subInfo.visibility = if (infoPref) View.VISIBLE else View.GONE
        val dark = preferences!!.getBoolean(SettingsActivity.DARK, false)
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
        defaultLoad = preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper").toString()
        changeChipColor(-1)
        when (preferences!!.getInt(SettingsActivity.SORT_METHOD, HOT)) {
            HOT -> {
                adapter = ImageAdapter(this, hotImages)
                currentSort = HOT
                changeChipColor(1)
            }
            NEW -> {
                adapter = ImageAdapter(this, newImages)
                currentSort = NEW
                changeChipColor(0)
            }
            else -> {
                adapter = ImageAdapter(this, topImages)
                currentSort = TOP
                changeChipColor(2)
            }
        }
        imageScroll.adapter = adapter
        binding.hotChip.setOnClickListener(this)
        binding.newChip.setOnClickListener(this)
        binding.topChip.setOnClickListener(this)
        defaultLoad = (if (intent.getBooleanExtra(OVERRIDE, false)) {
            intent.getStringExtra(SAVED)
        } else {
            if (FIRST_RUN) {
                FIRST_RUN = false
                preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper")
            } else {
                preferences!!.getString(QUERY, preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper"))
            }
        }).toString()
        queryString = defaultLoad
        binding.search.hint = defaultLoad
        preferences?.edit()?.putString(QUERY, queryString)?.apply()

        if (networkAvailable()) {
            imageJob = uiScope.launch {
                loadSubInfo(queryString)
                loadImages(getCon(), defaultLoad, true, getList())
            }
        } else {
            binding.info.visibility = View.VISIBLE
            binding.info.text = "No Network"
        }
        imageScroll.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (dy > 100) {
                        if (infoPref) {
                            binding.subInfo.visibility = View.GONE
                            infoShown = true
                            toggleSubInfo(binding.subInfoLayout)
                        }
                    }
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
                } else {
                    binding.bottomLoad.visibility = View.INVISIBLE
                    if (infoPref) {
                        if (dy != 0 && dy < -100) {
                            binding.subInfo.visibility = View.VISIBLE
                            infoShown = true
                            toggleSubInfo(binding.subInfoLayout)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isRunning = scrollJob?.isActive ?: false

                if (!recyclerView.canScrollVertically(1) && isRunning) {
                    binding.bottomLoad.visibility = View.VISIBLE
                } else if (!recyclerView.canScrollVertically(1) && !isRunning) {
                    binding.bottomLoad.visibility = View.VISIBLE
                    cancelThreads()

                    scrollJob = uiScope.launch {
                        loadImages(getCon(), if (queryString.isEmpty()) defaultLoad else queryString, false, getList())
                    }
                } else {
                    binding.bottomLoad.visibility = View.INVISIBLE
                }
            }
        })

        binding.search.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch(binding.searchButton)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun changeChipColor(highlight: Int) {
        binding.newChip.setChipBackgroundColorResource(R.color.white)
        binding.hotChip.setChipBackgroundColorResource(R.color.white)
        binding.topChip.setChipBackgroundColorResource(R.color.white)
        binding.newChip.setTextColor(Color.BLACK)
        binding.hotChip.setTextColor(Color.BLACK)
        binding.topChip.setTextColor(Color.BLACK)
        when (highlight) {
            0 -> {
                binding.newChip.setChipBackgroundColorResource(R.color.chip)
                binding.newChip.setTextColor(Color.WHITE)
            }
            1 -> {
                binding.hotChip.setChipBackgroundColorResource(R.color.chip)
                binding.hotChip.setTextColor(Color.WHITE)
            }
            2 -> {
                binding.topChip.setChipBackgroundColorResource(R.color.chip)
                binding.topChip.setTextColor(Color.WHITE)
            }
            else -> return
        }
    }

    private fun getList(): ArrayList<BitURL> = when (currentSort) {
        NEW -> newImages
        HOT -> hotImages
        else -> topImages
    }

    private fun getCon(): Context {
        return this
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.e("DESTROY", "DESTROY")
        cancelThreads()
    }

    private fun networkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @InternalCoroutinesApi
    fun startSearch(view: View) {
        binding.subInfo.visibility = if (infoPref) View.VISIBLE else View.GONE

        AFTER_HOT = ""
        AFTER_NEW = ""
        AFTER_TOP = ""
        cancelThreads()
        queryString = ""
        binding.loading.visibility = View.VISIBLE
        binding.info.visibility = View.INVISIBLE
        newImages.clear()
        hotImages.clear()
        topImages.clear()
        adapter!!.notifyDataSetChanged()
        queryString = binding.search.text.toString().replace(" ", "")
        val inputManager: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        if (networkAvailable()) {
            if (queryString.isNotEmpty()) {
                preferences?.edit()?.putString(QUERY, queryString)?.apply()
                imageJob = uiScope.launch {
                    loadSubInfo(queryString)
                    loadImages(getCon(), queryString, true, getList())
                }
            } else {
                queryString = defaultLoad
                preferences?.edit()?.putString(QUERY, queryString)?.apply()
                imageJob = uiScope.launch {
                    loadSubInfo(queryString)
                    loadImages(getCon(), defaultLoad, true, getList())
                }
            }
        } else {
            binding.info.visibility = View.VISIBLE
            binding.info.text = "No Network"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
            R.id.history -> {
                val launchHist = Intent(this, HistoryActivity::class.java)
                startActivity(launchHist)
                return true
            }
            R.id.subreddits -> {
                val launchSubs = Intent(this, SubActivity::class.java)
                startActivity(launchSubs)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelThreads() {

        if (imageJob != null) {
            binding.loading.visibility = View.GONE
            imageJob!!.cancel()
        }

        if (scrollJob != null) {
            binding.bottomLoad.visibility = View.GONE
            scrollJob!!.cancel()
        }
    }

    @InternalCoroutinesApi
    private fun runQuery() {
        imageJob = uiScope.launch {
            loadImages(getCon(), if (queryString.isEmpty()) defaultLoad else queryString, true, getList())
        }
    }

    override fun onBackPressed() {
        val confirmExit =
                MaterialAlertDialogBuilder(getCon(), R.style.MyThemeOverlayAlertDialog).apply {
                    setTitle("Exit App")
                    setMessage("Do you want to exit the app?")

                    setPositiveButton("Yes") { _, _ ->
                        (getCon() as Activity).finishAffinity()
                    }
                    setNegativeButton("No") { _, _ ->
                        //Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                        Snackbar.make(binding.root, "Cancelled", Snackbar.LENGTH_SHORT).show()
                    }
                }
        confirmExit.show()
    }

    @InternalCoroutinesApi
    override fun onClick(view: View) {
        binding.subInfo.visibility = if (infoPref) View.VISIBLE else View.GONE

        val temp: String? = when (currentSort) {
            NEW -> AFTER_NEW
            HOT -> AFTER_HOT
            TOP -> AFTER_TOP
            else -> null
        }
        if (imageJob != null && imageJob!!.isActive && temp == null) {
            //Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Please wait", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (imageJob != null && imageJob!!.isActive || scrollJob != null && scrollJob!!.isActive) {
            cancelThreads()
        }

        adapter!!.notifyDataSetChanged()
        val prefEdit = preferences!!.edit()
        if (view == binding.hotChip) {
            currentSort = HOT
            Log.e("CLICk", "CLICKED HOT")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, HOT)
            prefEdit.apply()
            changeChipColor(1)
            if (hotImages.isNotEmpty()) {
                adapter!!.setList(hotImages)
            } else {
                adapter!!.setList(hotImages)
                runQuery()
            }
        } else if (view == binding.newChip) {
            currentSort = NEW
            Log.e("CLICk", "CLICKED NEW")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, NEW)
            prefEdit.apply()
            changeChipColor(0)
            if (newImages.isNotEmpty()) {
                adapter!!.setList(newImages)
            } else {
                adapter!!.setList(newImages)
                runQuery()
            }
        } else if (view == binding.topChip) {
            currentSort = TOP
            Log.e("CLICk", "CLICKED TOP")
            prefEdit.putInt(SettingsActivity.SORT_METHOD, TOP)
            prefEdit.apply()
            changeChipColor(2)
            if (topImages.isNotEmpty()) {
                adapter!!.setList(topImages)
            } else {
                adapter!!.setList(topImages)
                runQuery()
            }
        }
    }

    private suspend fun loadSubInfo(query: String) {
        binding.subName.text = "Loading..."
        binding.subName.setCompoundDrawables(ContextCompat.getDrawable(applicationContext, R.drawable.ic_android),
                null, null, null)
        binding.subSubs.text = "Loading..."
        binding.subDesc.text = "Loading..."
        withContext(Dispatchers.Default) {
            try {
                val json = async { getSubInfo(query) }
                val result = json.await().getJSONObject("data")
                val iconUrl = result.getString("icon_img")
                val title = result.getString("display_name_prefixed")
                val desc = result.getString("public_description")
                val subs = result.getInt("subscribers")
                Log.e("ICON", iconUrl.toString())
                if (iconUrl.isNotEmpty() || iconUrl.isNotBlank()) {
                    Glide.with(getCon())
                            .asBitmap()
                            .load(iconUrl)
                            .override(200, 200)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    Log.e("FLAG", "GOT HERE")
                                    binding.subIcon.setImageBitmap(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                }
                withContext(Dispatchers.Main) {
                    binding.subName.text = title
                    binding.subSubs.text = "Subscribers: ${NumberFormat.getNumberInstance(Locale.US).format(subs)}"
                    binding.subDesc.text = desc
                    if (iconUrl.isEmpty() || iconUrl.isBlank()) {
                        Log.e("FLAG", "GOT HERE2")
                        val def = ContextCompat.getDrawable(applicationContext, R.drawable.ic_android)
                        def?.setTint(Color.BLACK)
                        binding.subIcon.setImageDrawable(def)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun loadImages(con: Context, query: String, first: Boolean, images: ArrayList<BitURL>) {
        if (first) {
            binding.loading.visibility = View.VISIBLE
        }
        binding.info.visibility = View.GONE
        withContext(Dispatchers.IO) {
            val json = QueryRequest.getQueryJson(query, first, con)
            withContext(Dispatchers.Default) {
                QueryRequest.loadImgsFromJSON(json, adapter, con, images, if (first) binding.loading else binding.bottomLoad, first)
            }
        }
        adapter?.notifyDataSetChanged()
        if (first) {
            binding.loading.visibility = View.GONE
        } else {
            binding.bottomLoad.visibility = View.INVISIBLE
        }

        if (first && adapter?.itemCount == 0) {
            Log.e("LIST", getList().toString())
            binding.info.visibility = View.VISIBLE
            binding.info.text = "Subreddit does not exist or it has no images"
        }
    }

    fun toggleSubInfo(view: View) {
        if (infoPref) {
            infoShown = !infoShown
            binding.subInfoLayout.visibility = if (infoShown) View.VISIBLE else View.GONE
            binding.infoTitle.visibility = if (infoShown) View.GONE else View.VISIBLE
        }
    }

    companion object {
        var FIRST_RUN = true
        const val SharedPrefFile = "com.mehul.redditwall"
        const val SAVED = "SAVED"
        const val OVERRIDE = "OVERRIDE"
        const val QUERY = "QUERY"
        const val NEW = 0
        const val HOT = 1
        const val TOP = 2
        var AFTER_NEW: String? = null
        var AFTER_HOT: String? = null
        var AFTER_TOP: String? = null

        @RequiresApi(Build.VERSION_CODES.Q)
        fun saveBitmap(con: Context, bitmap: Bitmap, name: String) {
            val relativeLocation: String = Environment.DIRECTORY_PICTURES + File.separator + "RedditWalls"

            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

            val resolver = con.contentResolver

            var stream: OutputStream? = null
            var uri: Uri? = null

            try {
                val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                uri = resolver.insert(contentUri, contentValues)
                if (uri == null) {
                    throw IOException("Failed to create new MediaStore record.")
                }
                Log.e("URI", uri.toString())
                stream = resolver.openOutputStream(uri)
                if (stream == null) {
                    throw IOException("Failed to get output stream.")
                }
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException("Failed to save bitmap.")
                }

            } catch (e: IOException) {
                if (uri != null) {
                    resolver.delete(uri, null, null)
                }
                throw e
            } finally {
                stream?.close()
            }
        }

        fun getDimensions(con: Context): IntArray {
            val disp = DisplayMetrics()
            (con as Activity).windowManager.defaultDisplay.getMetrics(disp)
            return intArrayOf(disp.widthPixels, disp.heightPixels)
        }

        suspend fun getSubInfo(subName: String): JSONObject {
            var subInfo = JSONObject()
            withContext(Dispatchers.Default) {
                val endpoint = "https://www.reddit.com/r/$subName/about/.json"
                val jsonString = async { getJsonData(endpoint) }
                try {
                    subInfo = JSONObject(jsonString.await())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            return subInfo
        }

        suspend fun getJsonData(endpoint: String): String {
            var jsonString = ""
            withContext(Dispatchers.IO) {
                var urlConnection: HttpURLConnection? = null
                var reader: BufferedReader? = null
                try {
                    val requestURL = URL(endpoint)

                    urlConnection = requestURL.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.connect()

                    val inputStream = urlConnection.inputStream
                    reader = BufferedReader(InputStreamReader(inputStream))
                    val builder = StringBuilder()

                    var line: String? = reader.readLine()
                    while (line != null) {
                        if (!isActive) {
                            break
                        }

                        builder.append(line)
                        builder.append("\n")
                        line = reader.readLine()
                    }

                    if (!isActive) {
                        jsonString = ""
                    }

                    if (builder.isEmpty()) {
                        jsonString = ""
                    }

                    jsonString = builder.toString()

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                    if (reader != null) {
                        try {
                            reader.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
            Log.e("JSON", jsonString)
            return jsonString
        }
    }
}