package com.mehul.redditwall

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide.with
import com.google.gson.Gson
import com.mehul.redditwall.favorites.FavImage
import com.mehul.redditwall.favorites.FavViewModel
import com.mehul.redditwall.history.HistViewModel
import com.mehul.redditwall.history.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

@Suppress("PrivatePropertyName")
class WallActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private var jsonList: String? = ""
    private var notifyManager: NotificationManager? = null
    private var wallPreview: ImageView? = null
    private var isGif: Boolean = false
    private var fromFav: Boolean = false
    private val WRITE = 1231
    private var index: Int = 0
    private var width: Int = 0
    private var height: Int = 0
    private var fname: String? = null
    private var imgUrl: String? = null
    private var imageList: ArrayList<BitURL> = ArrayList()
    private var detector: GestureDetector? = null
    private var task: LoadImages? = null
    private var preferences: SharedPreferences? = null
    private var filledStar: Drawable? = null
    private var openStar: Drawable? = null
    private var starred: Menu? = null
    private var load: ProgressBar? = null
    private var startUp: StartUp? = null
    private var favViewModel: FavViewModel? = null
    private var histViewModel: HistViewModel? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var query: String? = null
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
        setContentView(R.layout.activity_wall)
        histViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        query = preferences!!.getString(MainActivity.QUERY,
                preferences!!.getString(SettingsActivity.DEFAULT, "mobilewallpaper"))
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
        val bitmap = (wallPreview!!.drawable as BitmapDrawable).bitmap
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Where?")
                .setItems(R.array.location_options) { _, index ->
                    temp.show()
                    val wallLoc = if (index == 0) {
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

                    if (index == 0 || index == 1) {
                        try {
                            var wallSource = 0
                            assert(wall != null)
                            if (wallLoc == 0) {
                                wall?.setBitmap(bitmap)
                                wallSource = HistoryItem.BOTH
                            } else {
                                wall?.setBitmap(bitmap, null, true, wallLoc)
                                wallSource = if (wallLoc == WallpaperManager.FLAG_LOCK) HistoryItem.LOCK_SCREEN else HistoryItem.HOME_SCREEN
                            }
                            Toast.makeText(con, "successfully changed wallpaper", Toast.LENGTH_SHORT).show()
                            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query!!,
                                    SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(Date()),
                                    wallSource, imgUrl!!, imageList[index].postLink)
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
                            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query!!,
                                    SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(Date()),
                                    HistoryItem.BOTH, imgUrl!!, imageList[index].postLink)
                            histViewModel?.insert(histItem)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error setting wallpaper")
                            Toast.makeText(con, "failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        builder.create().show()//date, source, url, post
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        } else if (item.itemId == R.id.fav_image) {
            for (img in favViewModel!!.favList!!) {
                if (imgUrl!!.equals(img?.favUrl, ignoreCase = true)) {
                    item.icon = openStar
                    favViewModel?.deleteFavImage(img)
                    Toast.makeText(this, "Unfavorited", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            item.icon = filledStar
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
            favViewModel?.insert(FavImage((Math.random() * 10000).toInt() + 1, imgUrl!!, isGif, imageList[index].postLink))
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.wall_menu, menu)
        load = findViewById(R.id.load_more)
        wallPreview = findViewById(R.id.wall_holder)
        detector = GestureDetector(this, this)
        val incoming = intent
        fromFav = incoming.getBooleanExtra(FROM_FAV, true)
        jsonList = incoming.getStringExtra(LIST)
        uiScope.launch {
            if (jsonList != null) {
                imageList = jsonToList(jsonList!!)
            }
        }
        index = incoming.getIntExtra(INDEX, 0)
        imgUrl = incoming.getStringExtra(WALL_URL)
        isGif = incoming.getBooleanExtra(GIF, false)
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        createNotificationChannel()
        val disp = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(disp)
        width = preferences!!.getInt(SettingsActivity.IMG_WIDTH, disp.widthPixels)
        height = preferences!!.getInt(SettingsActivity.IMG_HEIGHT, disp.heightPixels)
        filledStar = ContextCompat.getDrawable(applicationContext, R.drawable.ic_star_black)
        openStar = ContextCompat.getDrawable(applicationContext, R.drawable.ic_star_open)
        starred = menu
        //TODO: Replace this monstrosity with coroutines
        startUp = StartUp(this, width, height, isGif, fromFav,
                wallPreview, starred, load, filledStar, openStar, favViewModel?.favList)
        startUp?.execute(imgUrl)
        return true
    }

    fun launchPost(view: View) {
        //from main, jsonList, currindex, imgUrl, isGif
        val currPost = imageList[index]
        val postIntent = Intent(this, PostActivity::class.java)
        postIntent.putExtra(PostActivity.POST_LINK, currPost.postLink)
        startActivity(postIntent)
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
        val bitmap = (wallPreview!!.drawable as BitmapDrawable).bitmap
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/RedditWalls")
        myDir.mkdirs()
        fname = SimpleDateFormat("MM-dd-yyyy 'at' hh-mm-ss", Locale.CANADA).format(Date())
                .replace(" ", "") + ".jpg"
        val file = File(myDir, fname!!)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            MediaStore.Images.Media.insertImage(contentResolver, file.absolutePath, file.name, file.name)
            sendNotification()
            val histItem = HistoryItem((Math.random() * 10000).toInt() + 1, query!!,
                    SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(Date()),
                    HistoryItem.DOWNLOADED, imgUrl!!, imageList[index].postLink)
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
        //send notification through the channel in the manager
        notifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())
        updateNotification()
    }

    private fun updateNotification() {
        val bitmap = (wallPreview!!.drawable as BitmapDrawable).bitmap
        val notifyBuilder = notificationBuilder
        notifyBuilder.setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .setBigContentTitle("Finished Downloading!"))
        notifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    /*public void cancelNotification() {
        notifyManager.cancel(NOTIFICATION_ID);
    }*/

    //creating a channel and putting it in the manager
    private fun createNotificationChannel() {
        notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //checking to see if the build version is greater than that of oreo to implement notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Download Notification", NotificationManager.IMPORTANCE_HIGH)

            //configuring notification settings when sent
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
        if (task != null)
            task!!.cancel(true)

        if (startUp != null)
            startUp!!.cancel(true)
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (task != null)
            task!!.cancel(true)

        if (startUp != null)
            startUp!!.cancel(true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (detector != null) this.detector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun swipedRight() {
        Log.e("R", "Right")
        if ((index - 1) >= 0) {
            index--
            val curr = imageList[index]
            imgUrl = curr.url
            isGif = curr.hasGif()

            when {
                startUp == null -> {
                    startUp = StartUp(this, width, height, isGif, fromFav, wallPreview,
                            starred, load, filledStar, openStar, favViewModel!!.favList)
                    startUp?.execute(imgUrl)
                }
                startUp!!.status == AsyncTask.Status.RUNNING ->
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                else -> {
                    startUp?.cancel(true)
                    startUp = StartUp(this, width, height, isGif, fromFav, wallPreview,
                            starred, load, filledStar, openStar, favViewModel!!.favList)
                    startUp?.execute(imgUrl)
                }
            }
        } else {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show()
        }
    }

    private fun swipedLeft() {
        Log.e("L", "LEFT")
        val inBound = index + 1 < imageList.size
        if (inBound) {
            index++
            val curr = imageList[index]
            imgUrl = curr.url
            isGif = curr.hasGif()

            when {
                startUp == null -> {
                    startUp = StartUp(this, width, height, isGif, fromFav, wallPreview,
                            starred, load, filledStar, openStar, favViewModel!!.favList)
                    startUp?.execute(imgUrl)
                }
                startUp!!.status == AsyncTask.Status.RUNNING ->
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                else -> {
                    startUp?.cancel(true)
                    startUp = StartUp(this, width, height, isGif, fromFav, wallPreview,
                            starred, load, filledStar, openStar, favViewModel!!.favList)
                    startUp?.execute(imgUrl)
                }
            }

        } else if (task == null && fromFav) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            task = LoadImages(this, load, imageList)
            task!!.execute(query)
        } else if (task != null && task?.status == AsyncTask.Status.RUNNING && fromFav) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show()
        } else if (task != null && task?.status != AsyncTask.Status.RUNNING && fromFav) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
            task?.cancel(true)
            task = LoadImages(this, load, imageList)
            task!!.execute(query)
        } else {
            Toast.makeText(this, "Reached the end", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFling(evt1: MotionEvent, evt2: MotionEvent, vX: Float, vY: Float): Boolean {
        var ret = false
        try {
            val diffY = evt2.y - evt1.y
            val diffX = evt2.x - evt1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > abs(diffY) && abs(diffX) > 100 && abs(vX) > 100) {
                    if (diffX > 0)
                        swipedRight()
                    else
                        swipedLeft()
                    ret = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
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

    //TODO: replace with coroutines and parse json on separate thread as well
    private class StartUp internal constructor(con: Context, val w: Int, val h: Int, val g: Boolean,
                                               val fm: Boolean, image: ImageView?, menu: Menu?, load: ProgressBar?,
                                               filled: Drawable?, open: Drawable?,
                                               f: List<FavImage?>?) : AsyncTask<String, Void, Boolean>() {
        var imgView: WeakReference<ImageView?> = WeakReference(image)
        var starMenu: WeakReference<Menu?> = WeakReference(menu)
        var loading: WeakReference<ProgressBar?> = WeakReference(load)
        var fillStar: WeakReference<Drawable?> = WeakReference(filled)
        var openStar: WeakReference<Drawable?> = WeakReference(open)
        var context: WeakReference<Context> = WeakReference(con)
        var favs: WeakReference<List<FavImage?>?> = WeakReference(f)
        lateinit var iurl: String

        override fun onPreExecute() {
            super.onPreExecute()
            loading.get()?.visibility = View.VISIBLE
            imgView.get()?.visibility = View.GONE
        }

        override fun doInBackground(vararg string: String): Boolean {
            iurl = string[0]
            var saved = false
            if (!fm) {
                for (fav in favs.get()!!) {
                    if (fav?.favUrl == string[0]) {
                        saved = true
                        break
                    }
                }
            } else {
                saved = true
            }

            return saved
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            starMenu.get()?.getItem(0)?.icon = if (result) {
                fillStar.get()
            } else {
                openStar.get()
            }
            loading.get()?.visibility = View.GONE
            imgView.get()?.visibility = View.VISIBLE

            if (g) {
                with(context.get()!!).asGif().load(iurl).override(w, h).centerCrop().into(imgView.get()!!)
            } else {
                with(context.get()!!).load(iurl).override(w, h).centerCrop().into(imgView.get()!!)
            }
        }
    }

    private class LoadImages internal constructor(con: Context?, load: ProgressBar?, imgs: ArrayList<BitURL>?)
        : AsyncTask<String, Void, Void?>() {
        internal var context: WeakReference<Context?> = WeakReference(con)
        internal var load: WeakReference<ProgressBar?> = WeakReference(load)
        internal var imgs: WeakReference<ArrayList<BitURL>?> = WeakReference(imgs)

        override fun onPreExecute() {
            super.onPreExecute()
            load.get()?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg strings: String): Void? {
            if (isCancelled) {
                return null
            }
            val rq = RestQuery(strings[0], context.get(), imgs.get(), null, load.get(), this)
            rq.getImages(rq.getQueryJson(false)!!)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (isCancelled) {
                return
            }
            load.get()?.visibility = View.GONE
            Toast.makeText(context.get(), "Done Loading", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private const val NOTIFICATION_ID = 0
        const val WALL_URL = "WALLURL"
        const val GIF = "GIF"
        const val LIST = "LIST"
        const val INDEX = "INDEX"
        const val FROM_FAV = "FAV_IMAGES"

        fun listToJson(imgs: ArrayList<BitURL>?): String {
            return Gson().toJson(imgs)
        }
    }
}
