package com.mehul.redditwall

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private var widthEdit: EditText? = null
    private var heightEdit: EditText? = null
    private var defaultEdit: EditText? = null
    private var scaleSeek: SeekBar? = null
    private var seekCount: TextView? = null
    private var preferences: SharedPreferences? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        preferences = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        seekCount = findViewById(R.id.scale_count)
        scaleSeek = findViewById(R.id.scale_seek)
        widthEdit = findViewById(R.id.width_edit)
        heightEdit = findViewById(R.id.height_edit)
        defaultEdit = findViewById(R.id.default_edit)
        val gifSwitch = findViewById<Switch>(R.id.gif_switch)
        gifSwitch.isChecked = preferences!!.getBoolean(LOAD_GIF, true)
        scaleSeek!!.progress = preferences!!.getInt(LOAD_SCALE, 0)
        seekCount!!.text = ((scaleSeek!!.progress + 1) * 2).toString() + "X"
        gifSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                preferences!!.edit().putBoolean(LOAD_GIF, true).apply()
            } else {
                preferences!!.edit().putBoolean(LOAD_GIF, false).apply()
            }
        }
        scaleSeek = findViewById(R.id.scale_seek)
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
        val disp = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(disp)
        val width = preferences!!.getInt(IMG_WIDTH, disp.widthPixels)
        val height = preferences!!.getInt(IMG_HEIGHT, disp.heightPixels)
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
        val disp = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(disp)
        var width = disp.widthPixels
        var height = disp.heightPixels
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
            Toast.makeText(this, "Saved Settings", Toast.LENGTH_SHORT).show()
        }

        val defaultSub = defaultEdit!!.text.toString()
        preferenceEditor.putString(DEFAULT, defaultSub)
        preferenceEditor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        //pref keys
        const val SORT_METHOD = "SORTIMG"
        const val IMG_WIDTH = "WIDTH"
        const val IMG_HEIGHT = "HEIGHT"
        const val DEFAULT = "DEFAULT"
        const val LOAD_SCALE = "LOAD"
        const val LOAD_GIF = "LOADGIF"
    }
}
