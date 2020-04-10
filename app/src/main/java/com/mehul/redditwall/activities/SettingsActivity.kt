package com.mehul.redditwall.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NavUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.ChangeWallpaper
import com.mehul.redditwall.R
import java.io.File


class SettingsActivity : AppCompatActivity() {
    private var widthEdit: EditText? = null
    private var heightEdit: EditText? = null
    private var defaultEdit: EditText? = null
    private var scaleSeek: SeekBar? = null
    private var intervalSeek: SeekBar? = null
    private var intervalCount: TextView? = null
    private var seekCount: TextView? = null
    private var preferences: SharedPreferences? = null
    private var dark = false
    private var alarmChanged = false
    private var stateChanged = false
    private var wallAlarm: AlarmManager? = null
    private var wallChangeIntent: Intent? = null
    private var pending: PendingIntent? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.elevation = 0F
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        wallChangeIntent = Intent(this, ChangeWallpaper::class.java)
        wallAlarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        wallChangeIntent?.action = "CHANGE_WALL"
        pending = PendingIntent.getBroadcast(this, 2, wallChangeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val refreshTitle = findViewById<TextView>(R.id.change_interval)
        val refreshLocSetting = findViewById<LinearLayout>(R.id.refresh_location_setting)
        refreshTitle.visibility = View.GONE
        intervalSeek = findViewById(R.id.interval_seek)
        intervalSeek!!.visibility = View.GONE
        intervalCount = findViewById(R.id.interval_count)
        intervalCount!!.visibility = View.GONE
        seekCount = findViewById(R.id.scale_count)
        scaleSeek = findViewById(R.id.scale_seek)
        widthEdit = findViewById(R.id.width_edit)
        heightEdit = findViewById(R.id.height_edit)
        defaultEdit = findViewById(R.id.default_edit)
        val randomSwitch = findViewById<Switch>(R.id.random_changer)
        val gifSwitch = findViewById<Switch>(R.id.gif_switch)
        val darkSwitch = findViewById<Switch>(R.id.dark_switch)
        val downloadOrigin = findViewById<Switch>(R.id.download_origin)
        dark = preferences!!.getBoolean(DARK, false)
        darkSwitch.isChecked = dark
        gifSwitch.isChecked = preferences!!.getBoolean(LOAD_GIF, false)
        downloadOrigin.isChecked = preferences!!.getBoolean(DOWNLOAD_ORIGIN, false)
        randomSwitch.isChecked = preferences!!.getBoolean(RANDOM_ENABLED, false)
        if (randomSwitch.isChecked) {
            refreshLocSetting.visibility = View.VISIBLE
            refreshTitle.visibility = View.VISIBLE
            intervalCount!!.visibility = View.VISIBLE
            intervalSeek!!.visibility = View.VISIBLE
        }
        intervalSeek!!.progress = preferences!!.getInt(RANDOM_INTERVAL, 0)
        intervalCount!!.text = (intervalSeek!!.progress + 1).toString() + " hrs"
        scaleSeek!!.progress = preferences!!.getInt(LOAD_SCALE, 0)
        seekCount!!.text = ((scaleSeek!!.progress + 1) * 2).toString() + "X"
        randomSwitch.setOnCheckedChangeListener { _, b ->
            preferences!!.edit().putBoolean(RANDOM_ENABLED, b).apply()
            alarmChanged = b
            if (b) {
                refreshLocSetting.visibility = View.VISIBLE
                intervalSeek!!.visibility = View.VISIBLE
                intervalCount!!.visibility = View.VISIBLE
                refreshTitle.visibility = View.VISIBLE
            } else {
                refreshLocSetting.visibility = View.GONE
                refreshTitle.visibility = View.GONE
                intervalSeek!!.visibility = View.GONE
                intervalCount!!.visibility = View.GONE
            }
        }
        gifSwitch.setOnCheckedChangeListener { _, b ->
            preferences!!.edit().putBoolean(LOAD_GIF, b).apply()
        }
        downloadOrigin.setOnCheckedChangeListener { _, b ->
            preferences!!.edit().putBoolean(DOWNLOAD_ORIGIN, b).apply()
        }
        darkSwitch.setOnCheckedChangeListener { _, b ->
            dark = b
            stateChanged = true
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
        }
        scaleSeek!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i > 0) {
                    seekCount!!.text = ((i + 1) * 2).toString() + "X"
                } else {
                    seekCount!!.text = "2X"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        intervalSeek!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                alarmChanged = true
                if (i > 0) {
                    intervalCount!!.text = (i + 1).toString() + " hrs"
                } else {
                    intervalCount!!.text = "1 hrs"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        val randButton = findViewById<Button>(R.id.random_location_button)
        when (preferences!!.getInt(RANDOM_LOCATION, 2)) {
            0 -> {
                randButton.text = "HOME"
            }

            1 -> {
                randButton.text = "LOCK"
            }

            2 -> {
                randButton.text = "BOTH"
            }
        }

        val dims = MainActivity.getDimensions(this)
        val width = preferences!!.getInt(IMG_WIDTH, dims[0])
        val height = preferences!!.getInt(IMG_HEIGHT, dims[1])
        widthEdit!!.setText(width.toString() + "")
        heightEdit!!.setText(height.toString() + "")

        val defaultSub = preferences!!.getString(DEFAULT, "mobilewallpaper")
        defaultEdit!!.setText(defaultSub)
    }

    public override fun onPause() {
        super.onPause()
        var valid = true
        val preferenceEditor = preferences!!.edit()
        preferenceEditor.putInt(LOAD_SCALE, scaleSeek!!.progress)
        preferenceEditor.putBoolean(DARK, dark)
        preferenceEditor.putInt(RANDOM_INTERVAL, intervalSeek!!.progress)
        val setRefresh = preferences!!.getBoolean(RANDOM_ENABLED, false) && alarmChanged

        if (setRefresh) {
            val interval = (intervalSeek!!.progress + 1) * 60 * 60 * 1000
            val triggerTime = SystemClock.elapsedRealtime() + interval
            if (wallAlarm != null) {
                Toast.makeText(this, "Random refresh enabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, interval.toLong(), pending)
            }
        } else {
            if (wallAlarm != null && !preferences!!.getBoolean(RANDOM_ENABLED, false)) {
                Toast.makeText(this, "Random refresh disabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.cancel(pending!!)
            }
        }
        val dims = MainActivity.getDimensions(this)
        var width = dims[0]
        var height = dims[1]
        try {
            width = Integer.parseInt(widthEdit!!.text.toString())
            height = Integer.parseInt(heightEdit!!.text.toString())
        } catch (e: NumberFormatException) {
            valid = false
            Toast.makeText(this, "You didn't enter a valid number", Toast.LENGTH_SHORT).show()
        }

        if (valid) {
            preferenceEditor.putInt(IMG_WIDTH, width)
            preferenceEditor.putInt(IMG_HEIGHT, height)
        }

        val defaultSub = defaultEdit!!.text.toString().replace(" ", "")
        preferenceEditor.putString(DEFAULT, defaultSub)
        preferenceEditor.apply()
    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        super.onBackPressed()
    }

    fun clearCache(view: View) {
        this.cacheDir.deleteRecursively()
        Toast.makeText(this, "Deleted Cache", Toast.LENGTH_SHORT).show()
    }

    companion object {
        //pref keys
        const val SORT_METHOD = "SORTIMG"
        const val IMG_WIDTH = "WIDTH"
        const val IMG_HEIGHT = "HEIGHT"
        const val DEFAULT = "DEFAULT"
        const val LOAD_SCALE = "LOAD"
        const val LOAD_GIF = "LOADGIF"
        const val DARK = "DARK"
        const val DOWNLOAD_ORIGIN = "DOWNLOAD_ORIGINAL"
        const val RANDOM_ENABLED = "SWITCHING_ENABLED"
        const val RANDOM_INTERVAL = "INTERVAL"
        const val RANDOM_LOCATION = "RAND_LOCATION"
        const val HOME = 0
        const val LOCK = 1
        const val BOTH = 2
    }

    fun setRandomLocation(view: View) {
        val but = view as Button
        val builder = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlayAlertDialog)
        builder.setTitle("Set Where?")
                .setItems(R.array.location_options) { _, i ->
                    when (i) {
                        0 -> {
                            but.text = "HOME"
                            preferences?.edit()?.putInt(RANDOM_LOCATION, HOME)?.apply()
                        }

                        1 -> {
                            but.text = "LOCK"
                            preferences?.edit()?.putInt(RANDOM_LOCATION, LOCK)?.apply()
                        }

                        2 -> {
                            but.text = "BOTH"
                            preferences?.edit()?.putInt(RANDOM_LOCATION, BOTH)?.apply()
                        }

                        else -> {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        builder.create().show()
    }

    fun deleteDownloads(view: View) {
        val root = Environment.getExternalStorageDirectory().toString()
        val downloads = File("$root/RedditWalls")
        if (downloads.exists()) {
            val pics = downloads.list()
            for (pic in pics) {
                val curr = File(downloads.path, pic)
                curr.delete()
            }

            downloads.delete()
        }

        Toast.makeText(this, "Deleted downloaded images", Toast.LENGTH_SHORT).show()
    }
}
