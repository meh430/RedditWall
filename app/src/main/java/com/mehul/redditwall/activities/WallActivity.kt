package com.mehul.redditwall.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide.with
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivityWallBinding
import com.mehul.redditwall.objects.BitURL
import com.mehul.redditwall.objects.FavImage
import com.mehul.redditwall.objects.HistoryItem
import com.mehul.redditwall.rest.RestQuery
import com.mehul.redditwall.viewmodels.FavViewModel
import com.mehul.redditwall.viewmodels.HistViewModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt

@Suppress("PrivatePropertyName", "DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WallActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private var jsonList: String? = ""
    private var notifyManager: NotificationManager? = null
    private var bottomUp = false
    private var isGif: Boolean = false
    private var downloadOriginal = false
    private var fromFav: Boolean = false
    private var fromHist: Boolean = false
    private var noQuery: Boolean = false
    private var index: Int = 0
    private var width: Int = 0
    private var height: Int = 0
    private var fname: String = ""
    private var imgUrl: String = ""
    private var postLink: String = ""
    private var imageList: ArrayList<BitURL> = ArrayList()
    private var detector: GestureDetector? = null
    private var imageJob: Job? = null
    private var preferences: SharedPreferences? = null
    private var filledStar: Drawable? = null
    private var openStar: Drawable? = null
    private var starred: Menu? = null
    private var favViewModel: FavViewModel? = null
    private var histViewModel: HistViewModel? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var query = ""
    private var currentBitmap: Bitmap? = null
    private lateinit var binding: ActivityWallBinding
    private val notificationBuilder: NotificationCompat.Builder
        get() {
            //TODO: replace deprecated methods with scoped storage solutions
            val notificationIntent = Intent()
            notificationIntent.action = Intent.ACTION_VIEW
            notificationIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory()
                    .toString() + "/RedditWalls/" + fname), "image/*")
            val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            notifyBuilder.setContentIntent(notificationPendingIntent).setAutoCancel(true).setContentTitle("Image has been downloaded!")
                    .setContentText("View the Image!").setSmallIcon(R.drawable.ic_download).setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
            return notifyBuilder
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F
        histViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        AFTER_HOT_WALL = MainActivity.AFTER_HOT
        AFTER_NEW_WALL = MainActivity.AFTER_NEW
        AFTER_TOP_WALL = MainActivity.AFTER_TOP
    }


    @SuppressLint("NewApi")
    fun setWallpaper(view: View) {
        if (isGif) {
            Toast.makeText(this, "GIF support is coming soon", Toast.LENGTH_SHORT).show()
            return
        }
        val con = this
        val temp = Toast.makeText(this, "Setting wallpaper...", Toast.LENGTH_LONG)
        val wall: WallpaperManager? = this.applicationContext.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
        if (binding.wallHolder.drawable == null) {
            Toast.makeText(this, "LOADING...", Toast.LENGTH_SHORT).show()
            return
        }
        val bitmap = (binding.wallHolder.drawable as BitmapDrawable).bitmap
        val builder = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlayAlertDialog)
        builder.setTitle("Set Where?")
                .setItems(R.array.location_options) { _, i ->
                    temp.show()
                    val wallLoc = if (i == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            WallpaperManager.FLAG_SYSTEM
                        } else {
                            0
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            WallpaperManager.FLAG_LOCK
                        } else {
                            0
                        }
                    }

                    if (i == 0 || i == 1) {
                        try {
                            assert(wall != null)
                            val wallSource: Int = if (wallLoc == 0) {
                                wall?.setBitmap(bitmap)
                                HistoryItem.BOTH
                            } else {
                                wall?.setBitmap(bitmap, null, true, wallLoc)
                                if (wallLoc == WallpaperManager.FLAG_LOCK) HistoryItem.LOCK_SCREEN else HistoryItem.HOME_SCREEN
                            }
                            Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show()
                            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query,
                                    SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date()),
                                    wallSource, imgUrl, imageList[index].postLink)
                            histViewModel?.insert(histItem)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error setting wallpaper")
                            Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        try {
                            assert(wall != null)
                            wall?.setBitmap(bitmap)
                            Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show()
                            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query,
                                    SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date()),
                                    HistoryItem.BOTH, imgUrl, imageList[index].postLink)
                            histViewModel?.insert(histItem)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error setting wallpaper")
                            Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        builder.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        } else if (item.itemId == R.id.fav_image) {
            val favs = favViewModel!!.favList
            for (img in favs) {
                if (imgUrl.equals(img.favUrl, ignoreCase = true)) {
                    item.icon = openStar
                    favViewModel?.deleteFavImage(img)
                    Toast.makeText(this, "Unfavorited", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            item.icon = filledStar
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            favViewModel?.insert(FavImage((Math.random() * 10000).toInt() + 1, imgUrl,
                    isGif, imageList[index].postLink, query))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun jsonToList(json: String): ArrayList<BitURL> {
        val ret = ArrayList<BitURL>()
        withContext(Dispatchers.Default) {
            try {
                val list = JSONArray(json)
                for (i in 0 until list.length()) {
                    val curr = list.getJSONObject(i)
                    var gif = false
                    if (curr.getBoolean("gif")) {
                        gif = true
                    }
                    val temp = BitURL(null, curr.getString("url"), curr.getString("post"))
                    temp.setGif(gif)
                    withContext(Dispatchers.Main) {
                        ret.add(temp)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return ret
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.wall_menu, menu)
        detector = GestureDetector(this, this)
        val incoming = intent
        fromFav = incoming.getBooleanExtra(FROM_FAV, false)
        fromHist = incoming.getBooleanExtra(FROM_HIST, false)
        noQuery = fromFav || fromHist
        jsonList = incoming.getStringExtra(LIST)
        uiScope.launch {
            if (jsonList != null && jsonList!!.isNotEmpty()) {
                imageList = jsonToList(jsonList!!)
            }
        }
        index = incoming.getIntExtra(INDEX, 0)
        imgUrl = incoming.getStringExtra(WALL_URL) ?: ""
        postLink = incoming.getStringExtra(PostActivity.POST_LINK) ?: ""
        isGif = incoming.getBooleanExtra(GIF, false)
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        downloadOriginal = preferences!!.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)
        createNotificationChannel()
        val dims = MainActivity.getDimensions(this)
        width = preferences!!.getInt(SettingsActivity.IMG_WIDTH, dims[0])
        height = preferences!!.getInt(SettingsActivity.IMG_HEIGHT, dims[1])
        query = if (noQuery) {
            incoming.getStringExtra(FAV_LIST)
        } else {
            incoming.getStringExtra(MainActivity.QUERY)
        }.toString()

        val open = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_open_dark
        } else {
            R.drawable.ic_open_light
        }
        val filled = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_filled_dark
        } else {
            R.drawable.ic_filled_light
        }
        filledStar = ContextCompat.getDrawable(applicationContext, filled)
        openStar = ContextCompat.getDrawable(applicationContext, open)
        starred = menu
        binding.subreddit.text = "Subreddit: $query"
        uiScope.launch {
            startUp(getCon())
        }
        return true
    }

    fun launchPost(view: View) {
        if (binding.postTitle.text.contains("Loading")) {
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()
            return
        }

        val post = Intent(Intent.ACTION_VIEW, Uri.parse(postLink))

        if (post.resolveActivity(packageManager) != null) {
            startActivity(post)
        } else {
            val postIntent = Intent(this, PostActivity::class.java)
            postIntent.putExtra(PostActivity.POST_LINK, postLink)
            postIntent.putExtra(PostActivity.POST_TITLE, "${binding.postTitle.text}")
            startActivity(postIntent)
        }
    }

    fun downloadImage(view: View) {
        if (isGif) {
            Toast.makeText(this, "GIF support is coming soon", Toast.LENGTH_SHORT).show()
            return
        }
        //ask for storage permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE)
        } else {
            saveImage()
        }
    }

    @Suppress("DEPRECATION")
    private fun saveImage() {
        if (binding.wallHolder.drawable == null) {
            Toast.makeText(this, "LOADING...", Toast.LENGTH_SHORT).show()
            return
        }
        val bitmap = if (preferences!!.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)) {
            currentBitmap
        } else {
            (binding.wallHolder.drawable as BitmapDrawable).bitmap
        }
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/RedditWalls")
        myDir.mkdirs()
        fname = (0..999999999).random().toString().replace(" ", "") + ".jpg"

        val file = File(myDir, fname)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)
            sendNotification()
            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query,
                    SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date()),
                    HistoryItem.DOWNLOADED, imgUrl, imageList[index].postLink)
            histViewModel?.insert(histItem)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == WRITE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage()
            } else {
                Toast.makeText(this, "Cannot download, please grant permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendNotification() {
        val notifyBuilder = notificationBuilder
        notifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())
        updateNotification()
    }

    private fun updateNotification() {
        val bitmap = (binding.wallHolder.drawable as BitmapDrawable).bitmap
        val notifyBuilder = notificationBuilder
        notifyBuilder.setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setBigContentTitle("Finished Downloading!"))
        notifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun createNotificationChannel() {
        notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Download Notification", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.YELLOW
                enableVibration(true)
                description = "Notification for Download"
            }
            notifyManager!!.createNotificationChannel(notificationChannel)
        }
    }

    public override fun onStop() {
        super.onStop()
        if (imageJob != null)
            imageJob!!.cancel()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (imageJob != null)
            imageJob!!.cancel()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (detector != null) this.detector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun getCon(): Context {
        return this
    }

    private fun swipedRight() {
        if (jsonList!!.isEmpty()) {
            return
        }
        Log.e("R", "Right")
        if ((index - 1) >= 0) {
            index--
            val curr = imageList[index]
            postLink = curr.postLink
            imgUrl = curr.url
            isGif = curr.hasGif()
            val con = this
            uiScope.launch {
                startUp(con)
            }
        } else {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show()
        }
    }

    private fun swipedLeft() {
        if (jsonList!!.isEmpty()) {
            return
        }
        Log.e("L", "LEFT")
        val inBound = index + 1 < imageList.size
        if (inBound) {
            index++
            val curr = imageList[index]
            postLink = curr.postLink
            imgUrl = curr.url
            isGif = curr.hasGif()
            val con = this
            uiScope.launch {
                startUp(con)
            }
        } else if (imageJob == null && !noQuery) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            imageJob = uiScope.launch {
                loadImages(getCon(), query)
            }
        } else if (imageJob != null && imageJob!!.isActive && !noQuery) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show()
        } else if (imageJob != null && (!imageJob!!.isActive) && !noQuery) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            imageJob?.cancel()
            imageJob = uiScope.launch {
                loadImages(getCon(), query)
            }
        } else {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, vX: Float, vY: Float): Boolean {
        val SWIPE_DISTANCE_THRESHOLD = 50
        val SWIPE_VELOCITY_THRESHOLD = 50
        val distanceX: Float = e2.x - e1.x
        val distanceY: Float = e2.y - e1.y
        if (abs(distanceX) > abs(distanceY) && abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && abs(vX) > SWIPE_VELOCITY_THRESHOLD) {
            if (distanceX > 0) swipedRight() else swipedLeft()
            return true
        } else if (abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && abs(vY) > SWIPE_VELOCITY_THRESHOLD) {
            if (distanceY > 0) {
                bottomUp = false
                toggle(bottomUp)
            } else {
                bottomUp = true
                toggle(bottomUp)
            }
            return true
        }
        return false
    }

    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {}

    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(motionEvent: MotionEvent, motionEvent1: MotionEvent, v: Float, v1: Float): Boolean {
        return false
    }

    override fun onLongPress(motionEvent: MotionEvent) {}

    private fun convertUTC(utc: Long): String {
        val format = SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date(utc * 1000))
        val tempDate = format.trim().split(" at ")
        val date = tempDate[0].trim().split("-")
        val time = tempDate[1].trim().split(":")
        val month = HistoryItem.months[Integer.parseInt(date[0])]
        var hours = Integer.parseInt(time[0])
        val pmam = if (hours > 12) {
            hours -= 12
            "p.m"
        } else {
            "a.m"
        }
        return "$month ${HistoryItem.getOrdinal(Integer.parseInt(date[1]))}, ${date[2]} | ${hours}:${time[1]} $pmam"
    }

    @SuppressLint("SetTextI18n")
    private suspend fun loadUps() {
        binding.upvotes.text = "Upvotes: Loading..."
        binding.comments.text = "Comments: Loading..."
        binding.uploadDate.text = "Date: Loading..."
        binding.author.text = "Author: Loading..."
        binding.postTitle.text = "Loading..."
        binding.imageSize.text = "Size: Loading..."
        binding.subreddit.text = "Subreddit: Loading..."
        withContext(Dispatchers.Default) {
            val postJson = async { getPostJSON() }

            try {
                val jsonList = JSONArray(postJson.await())
                var json = jsonList.getJSONObject(0)
                json = json.getJSONObject("data")
                json = json.getJSONArray("children").getJSONObject(0).getJSONObject("data")
                val sub = json.getString("subreddit")
                val title = json.getString("title").trim()
                val ups = json.getInt("ups")
                val utcTime = json.getLong("created_utc")
                val author = json.getString("author")
                val commentNum = json.getString("num_comments")
                val uploadDate = convertUTC(utcTime)
                withContext(Dispatchers.Main) {
                    query = sub
                    binding.subreddit.text = "Subreddit: r/$sub"
                    binding.upvotes.text = "Upvotes: $ups"
                    binding.postTitle.text = title
                    binding.author.text = "Author: u/$author"
                    binding.comments.text = "Comments: $commentNum"
                    binding.uploadDate.text = "Date: $uploadDate"
                }

                while (binding.wallHolder.drawable == null) {
                }

                if (isGif) {
                    withContext(Dispatchers.Main) {
                        binding.imageSize.text = "Size: GIF"
                    }
                } else {
                    val bitmap = if (preferences!!.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)) {
                        currentBitmap
                    } else {
                        (binding.wallHolder.drawable as BitmapDrawable).bitmap
                    }

                    val stream = ByteArrayOutputStream()
                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val imageInByte = stream.toByteArray()
                    val length = imageInByte.size
                    val size = ((length / 1000000.00) * 100).roundToInt() / 100.00
                    withContext(Dispatchers.Main) {
                        binding.imageSize.text = "Size: $size MB"
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getPostJSON(): String {
        var jsonString = ""
        Log.e("POST", postLink)
        withContext(Dispatchers.IO) {
            val ENDPOINT = "$postLink.json"
            var urlConnection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val requestURL = URL(ENDPOINT)

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
        Log.e("JSONN", jsonString)
        return jsonString
    }

    private suspend fun startUp(con: Context) {
        binding.loadMore.visibility = View.VISIBLE
        binding.wallHolder.visibility = View.GONE
        var saved = false
        withContext(Dispatchers.Default) {
            val favs = favViewModel!!.favList
            if (!fromFav) {
                for (fav in favs) {
                    if (fav.favUrl == imgUrl) {
                        saved = true
                        break
                    }
                }
            } else {
                saved = true
            }
        }

        if (downloadOriginal) {
            withContext(Dispatchers.IO) {
                currentBitmap = with(con).asBitmap().load(imgUrl).submit().get()
            }
        }

        starred!!.getItem(0)?.icon = if (saved) {
            filledStar
        } else {
            openStar
        }
        binding.loadMore.visibility = View.GONE
        binding.wallHolder.visibility = View.VISIBLE

        if (isGif) {
            with(con).asGif().load(imgUrl).override(width, height).centerCrop().into(binding.wallHolder)
        } else {
            with(con).load(imgUrl).override(width, height).centerCrop().into(binding.wallHolder)
        }

        loadUps()
    }

    private suspend fun loadImages(con: Context?, queryString: String) {
        binding.loadMore.visibility = View.VISIBLE
        withContext(Dispatchers.IO) {
            val rq = RestQuery(queryString, con)
            val jsonRes = async { rq.getQueryJson() }
            val retImages = async { rq.getImages(jsonRes.await()) }
            withContext(Dispatchers.Main) {
                imageList.addAll(retImages.await())
            }
        }

        binding.loadMore.visibility = View.GONE
        Toast.makeText(con, "Done Loading", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private const val NOTIFICATION_ID = 0
        const val WRITE = 1231
        const val WALL_URL = "WALLURL"
        const val GIF = "GIF"
        const val LIST = "LIST"
        const val INDEX = "INDEX"
        const val FROM_FAV = "FAV_IMAGES"
        const val FROM_HIST = "HIST_IMAGES"
        const val FAV_LIST = "FAV_LIST"
        var AFTER_NEW_WALL: String? = null
        var AFTER_HOT_WALL: String? = null
        var AFTER_TOP_WALL: String? = null
    }

    fun launchSearch(view: View) {
        val launchMain = Intent(this, MainActivity::class.java)
        launchMain.apply {
            putExtra(MainActivity.SAVED, query)
            putExtra(MainActivity.OVERRIDE, true)
        }
        val clipboard: ClipboardManager? = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", query)
        assert(clipboard != null)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "Saved to clipboard", Toast.LENGTH_SHORT).show()
        finish()
        this.startActivity(launchMain)
    }

    fun toggleBottom(view: View) {
        bottomUp = !bottomUp
        toggle(bottomUp)
    }

    override fun onBackPressed() {
        if (bottomUp) {
            toggle(false)
            bottomUp = false
        } else {
            super.onBackPressed()
        }
    }

    private fun toggle(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 400
        transition.addTarget(R.id.bottomSheet)
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
        binding.bottomSheet.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            binding.expandButton.visibility = View.GONE
        } else {
            Handler().postDelayed(Runnable {
                binding.expandButton.visibility = View.VISIBLE
            }, 400)
        }
    }

    fun launchUser(view: View) {
        if (binding.author.text.contains("Loading")) {
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()
            return
        }
        var author = binding.author.text.trim().split(":")[1].trim()
        author = author.replace("u/", "")
        val user = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.reddit.com/user/$author/"))

        if (user.resolveActivity(packageManager) != null) {
            startActivity(user)
        } else {
            val postIntent = Intent(this, PostActivity::class.java)
            postIntent.putExtra(PostActivity.POST_LINK, "https://www.reddit.com/user/$author/")
            postIntent.putExtra(PostActivity.POST_TITLE, "u/${author}")
            startActivity(postIntent)
        }
    }
}
