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
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NavUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.ChangeWallpaper
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivitySettingsBinding
import java.io.File


class SettingsActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private var dark = false
    private var alarmChanged = false
    private var stateChanged = false
    private var wallAlarm: AlarmManager? = null
    private var wallChangeIntent: Intent? = null
    private var pending: PendingIntent? = null
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        wallChangeIntent = Intent(this, ChangeWallpaper::class.java)
        wallAlarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        wallChangeIntent?.action = "CHANGE_WALL"
        pending = PendingIntent.getBroadcast(this, 2, wallChangeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        binding.changeIntervalTitle.visibility = View.GONE
        dark = preferences.getBoolean(DARK, false)
        binding.darkSwitch.isChecked = dark
        binding.previewRes.isChecked = preferences.getBoolean(PREVIEW_RES, false)
        binding.gifSwitch.isChecked = preferences.getBoolean(LOAD_GIF, false)
        binding.downloadOrigin.isChecked = preferences.getBoolean(DOWNLOAD_ORIGIN, false)
        binding.randomSwitch.isChecked = preferences.getBoolean(RANDOM_ENABLED, false)
        showRandomSettings(binding.randomSwitch.isChecked)

        binding.intervalSeek.progress = preferences.getInt(RANDOM_INTERVAL, 0)
        binding.intervalCount.text = (binding.intervalSeek.progress + 1).toString() + " hrs"
        //binding.scaleSeek.progress = preferences.getInt(LOAD_SCALE, 0)
        //binding.scaleCount.text = ((binding.scaleSeek.progress + 1) * 2).toString() + "X"
        binding.randomSwitch.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(RANDOM_ENABLED, b).apply()
            alarmChanged = b
            showRandomSettings(b)
        }
        binding.gifSwitch.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(LOAD_GIF, b).apply()
        }
        binding.downloadOrigin.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(DOWNLOAD_ORIGIN, b).apply()
        }
        binding.previewRes.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(PREVIEW_RES, b).apply()
        }
        binding.darkSwitch.setOnCheckedChangeListener { _, b ->
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

        /*binding.scaleSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i > 0) {
                    binding.scaleCount.text = ((i + 1) * 2).toString() + "X"
                } else {
                    binding.scaleCount.text = "2X"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/
        binding.intervalSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                alarmChanged = true
                if (i > 0) {
                    binding.intervalCount.text = (i + 1).toString() + " hrs"
                } else {
                    binding.intervalCount.text = "1 hrs"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        val randButton = binding.randomLocationButton
        when (preferences.getInt(RANDOM_LOCATION, 2)) {
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

        val dimensions = AppUtils.getWallDimensions(this)
        binding.widthEdit.setText(dimensions[0].toString() + "")
        binding.heightEdit.setText(dimensions[1].toString() + "")

        val defaultSub = preferences.getString(DEFAULT, "mobilewallpaper")
        binding.defaultEdit.setText(defaultSub)
    }

    private fun showRandomSettings(show: Boolean) {
        val setting = if (show) View.VISIBLE else View.GONE
        binding.refreshLocationSetting.visibility = setting
        binding.randomSeekSection.visibility = setting
        binding.changeIntervalTitle.visibility = setting
    }

    public override fun onPause() {
        super.onPause()
        var valid = true
        val preferenceEditor = preferences.edit()
        //preferenceEditor.putInt(LOAD_SCALE, binding.scaleSeek.progress)
        preferenceEditor.putBoolean(DARK, dark)
        preferenceEditor.putInt(RANDOM_INTERVAL, binding.intervalSeek.progress)
        val setRefresh = preferences.getBoolean(RANDOM_ENABLED, false) && alarmChanged

        if (setRefresh) {
            val interval = (binding.intervalSeek.progress + 1) * 60 * 60 * 1000
            val triggerTime = SystemClock.elapsedRealtime() + interval
            if (wallAlarm != null) {
                Toast.makeText(this, "Random refresh enabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, interval.toLong(), pending)
            }
        } else {
            if (wallAlarm != null && !preferences.getBoolean(RANDOM_ENABLED, false)) {
                Toast.makeText(this, "Random refresh disabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.cancel(pending!!)
            }
        }
        val dims = AppUtils.getWallDimensions(this)
        var width = dims[0]
        var height = dims[1]
        try {
            width = Integer.parseInt(binding.widthEdit.text.toString())
            height = Integer.parseInt(binding.heightEdit.text.toString())
        } catch (e: NumberFormatException) {
            valid = false
            Toast.makeText(this, "You didn't enter a valid number", Toast.LENGTH_SHORT).show()
        }

        if (valid) {
            preferenceEditor.putInt(IMG_WIDTH, width)
            preferenceEditor.putInt(IMG_HEIGHT, height)
        }

        val defaultSub = binding.defaultEdit.text.toString().replace(" ", "")
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
        const val PREVIEW_RES = "PREVIEWRES"
        const val SHOW_INFO = "INFOCARD"
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
                            preferences.edit()?.putInt(RANDOM_LOCATION, HOME)?.apply()
                        }

                        1 -> {
                            but.text = "LOCK"
                            preferences.edit()?.putInt(RANDOM_LOCATION, LOCK)?.apply()
                        }

                        2 -> {
                            but.text = "BOTH"
                            preferences.edit()?.putInt(RANDOM_LOCATION, BOTH)?.apply()
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
