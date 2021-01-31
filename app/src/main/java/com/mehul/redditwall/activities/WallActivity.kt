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
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide.with
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivityWallBinding
import com.mehul.redditwall.objects.HistoryItem
import com.mehul.redditwall.objects.WallImage
import com.mehul.redditwall.viewmodels.FavViewModel
import com.mehul.redditwall.viewmodels.HistViewModel
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class WallActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private var fileName = ""
    private var imgUrl = ""
    private var postLink = ""
    private var subName = ""
    private var previewUrl = ""

    private var bottomUp = false
    private var downloadOriginal = false

    private var filledHeart: Drawable? = null
    private var openHeart: Drawable? = null

    private var detector: GestureDetector? = null
    private var favViewModel: FavViewModel? = null
    private var histViewModel: HistViewModel? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var currentBitmap: Bitmap? = null
    private lateinit var preferences: SharedPreferences

    private lateinit var startJob: Job

    private lateinit var binding: ActivityWallBinding

    private var notifyManager: NotificationManager? = null
    private val notificationBuilder: NotificationCompat.Builder
        get() {
            //TODO: replace deprecated methods with scoped storage solutions
            val notificationIntent = Intent()
            notificationIntent.action = Intent.ACTION_VIEW
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                notificationIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory()
                        .toString() + "/RedditWalls/" + fileName), "image/*")
            } else {
                notificationIntent.type = "image/*"
            }
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
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        histViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        preferences = AppUtils.getPreferences(this)

        detector = GestureDetector(this, this)
        val incoming = intent
        imgUrl = incoming.getStringExtra(IMAGE_URL) ?: ""
        postLink = incoming.getStringExtra(PostActivity.POST_LINK) ?: ""
        previewUrl = incoming.getStringExtra(PREVIEW_URL) ?: ""
        subName = incoming.getStringExtra(SUB_NAME) ?: ""

        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        downloadOriginal = preferences.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)
        createNotificationChannel()


        filledHeart = ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart_filled)
        openHeart = ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart)
        binding.subreddit.text = "Subreddit: $subName"
        startJob = uiScope.launch {
            startUp(getCon())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startJob.cancel()
    }

    @SuppressLint("NewApi")
    fun setWallpaper(view: View) {
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
                            val wallSource: Int = if (wallLoc == 0) {
                                wall?.setBitmap(bitmap)
                                HistoryItem.BOTH
                            } else {
                                wall?.setBitmap(bitmap, null, true, wallLoc)
                                if (wallLoc == WallpaperManager.FLAG_LOCK) HistoryItem.LOCK_SCREEN else HistoryItem.HOME_SCREEN
                            }
                            Toast.makeText(getCon(), "successfully changed wallpaper", Toast.LENGTH_SHORT).show()
                            val histItem = HistoryItem(subName = subName, setDate = Date().time,
                                    source = wallSource, imgUrl = imgUrl, postLink = postLink, previewUrl = previewUrl)
                            histViewModel?.insert(histItem)
                        } catch (e: Exception) {
                            Toast.makeText(getCon(), "failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        try {
                            wall?.setBitmap(bitmap)
                            Toast.makeText(getCon(), "successfully changed wallpaper", Toast.LENGTH_SHORT).show()
                            val histItem = HistoryItem(subName = subName, setDate = Date().time,
                                    source = HistoryItem.BOTH, imgUrl = imgUrl, postLink = postLink, previewUrl = previewUrl)
                            histViewModel?.insert(histItem)
                        } catch (e: Exception) {
                            Toast.makeText(getCon(), "failed to set wallpaper", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        builder.create().show()
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
        //ask for storage permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE)
        } else {
            saveImage()
        }
    }

    private fun saveImage() {
        if (binding.wallHolder.drawable == null) {
            Toast.makeText(this, "LOADING...", Toast.LENGTH_SHORT).show()
            return
        }
        val bitmap = if (preferences.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)) {
            currentBitmap
        } else {
            (binding.wallHolder.drawable as BitmapDrawable).bitmap
        }

        fileName = AppUtils.saveBitmap(bitmap!!, this)
        sendNotification()
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


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (detector != null) this.detector!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun getCon(): Context {
        return this
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, vX: Float, vY: Float): Boolean {
        val SWIPE_DISTANCE_THRESHOLD = 50
        val SWIPE_VELOCITY_THRESHOLD = 50
        val distanceX: Float = e2.x - e1.x
        val distanceY: Float = e2.y - e1.y
        if (abs(distanceX) > abs(distanceY) && abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && abs(vX) > SWIPE_VELOCITY_THRESHOLD) {
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
            val postJson = async { AppUtils.getJsonData("$postLink.json") }

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
                val uploadDate = AppUtils.convertUTC(utcTime * 1000)
                withContext(Dispatchers.Main) {
                    subName = sub
                    binding.subreddit.text = "Subreddit: r/$sub"
                    binding.upvotes.text = "Upvotes: $ups"
                    binding.postTitle.text = title
                    binding.author.text = "Author: u/$author"
                    binding.comments.text = "Comments: $commentNum"
                    binding.uploadDate.text = "Date: $uploadDate"
                }

                while (binding.wallHolder.drawable == null) {
                }

                val bitmap = if (preferences.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)) {
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
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun startUp(con: Context) {
        binding.loadMore.visibility = View.VISIBLE
        binding.wallHolder.visibility = View.GONE
        var saved = false
        withContext(Dispatchers.Default) {
            val favs = favViewModel!!.favList
            for (fav in favs) {
                if (fav.imgUrl == imgUrl) {
                    saved = true
                    break
                }
            }
        }

        var failed = false;

        if (downloadOriginal) {
            withContext(Dispatchers.IO) {
                try {
                    currentBitmap = with(con).asBitmap().load(imgUrl).submit().get()
                } catch (e: Exception) {
                    failed = true;
                    currentBitmap = BitmapFactory.decodeResource(resources, R.drawable.picerror)//with(con).asBitmap().load("https://github.com/meh430/RedditWall/blob/master/app/src/main/res/drawable/picerror.png?raw=true").submit().get();
                }
            }
        }

        binding.favoriteButton.setImageDrawable(if (saved) filledHeart else openHeart)
        binding.loadMore.visibility = View.GONE
        binding.wallHolder.visibility = View.VISIBLE

        if (failed) {
            Toast.makeText(con, "Error loading image", Toast.LENGTH_SHORT).show()
        }

        val dimensions = AppUtils.getWallDimensions(con)
        with(con).load(imgUrl).override(dimensions[0], dimensions[1]).centerCrop().into(binding.wallHolder)
        loadUps()
    }


    fun copySubName(view: View) {
        val clipboard: ClipboardManager? = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", subName)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "Saved to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun toggleBottom(view: View) {
        bottomUp = !bottomUp
        toggle(bottomUp)
    }

    override fun onBackPressed() {
        backPress(null)
    }

    private fun toggle(show: Boolean) {
        val transition: Transition = Slide(Gravity.BOTTOM)
        transition.duration = 400
        transition.addTarget(R.id.bottomSheet)
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
        binding.bottomSheet.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            binding.expandButton.visibility = View.GONE
            binding.favoriteButton.visibility = View.GONE
        } else {
            Handler().postDelayed(Runnable {
                binding.expandButton.visibility = View.VISIBLE
                binding.favoriteButton.visibility = View.VISIBLE
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

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        private const val NOTIFICATION_ID = 0
        const val WRITE = 1231

        const val IMAGE_URL = "IMAGE_URL"
        const val PREVIEW_URL = "PREVIEW_URL"
        const val SUB_NAME = "SUB_NAME"
    }

    fun favoriteImage(view: View) {
        val favs = favViewModel!!.favList
        for (img in favs) {
            if (imgUrl.equals(img.imgUrl, ignoreCase = true)) {
                binding.favoriteButton.setImageDrawable(openHeart)
                favViewModel?.deleteFavImage(img)
                return
            }
        }
        binding.favoriteButton.setImageDrawable(filledHeart)
        favViewModel?.insert(WallImage(imgUrl = imgUrl, postLink = postLink, subName = subName, previewUrl = previewUrl)
        )
    }

    fun backPress(view: View?) {
        if (bottomUp) {
            toggle(false)
            bottomUp = false
        } else {
            super.onBackPressed()
        }
    }
}
