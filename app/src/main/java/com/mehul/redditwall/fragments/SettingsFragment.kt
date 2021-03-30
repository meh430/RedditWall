package com.mehul.redditwall.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.AppUtils
import com.mehul.redditwall.ChangeWallpaper
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var dark = false
    private var alarmChanged = false
    private var stateChanged = false
    private var wallAlarm: AlarmManager? = null
    private var wallChangeIntent: Intent? = null
    private var pending: PendingIntent? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("RESET", "settings init")
        val context = requireContext()
        preferences = AppUtils.getPreferences(context)
        wallChangeIntent = Intent(context, ChangeWallpaper::class.java)
        wallAlarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        wallChangeIntent?.action = "CHANGE_WALL"
        pending = PendingIntent.getBroadcast(context, 2, wallChangeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        binding.changeIntervalTitle.visibility = View.GONE
        dark = preferences.getBoolean(SettingsActivity.DARK, false)
        binding.darkSwitch.isChecked = dark
        binding.previewRes.isChecked = preferences.getBoolean(SettingsActivity.PREVIEW_RES, false)
        binding.downloadOrigin.isChecked = preferences.getBoolean(SettingsActivity.DOWNLOAD_ORIGIN, false)
        binding.randomSwitch.isChecked = preferences.getBoolean(SettingsActivity.RANDOM_ENABLED, false)
        showRandomSettings(binding.randomSwitch.isChecked)

        binding.intervalSeek.progress = preferences.getInt(SettingsActivity.RANDOM_INTERVAL, 0)
        binding.intervalCount.text = (binding.intervalSeek.progress + 1).toString() + " hrs"
        //binding.scaleSeek.progress = preferences.getInt(LOAD_SCALE, 0)
        //binding.scaleCount.text = ((binding.scaleSeek.progress + 1) * 2).toString() + "X"
        binding.randomSwitch.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(SettingsActivity.RANDOM_ENABLED, b).apply()
            alarmChanged = b
            showRandomSettings(b)
        }
        binding.downloadOrigin.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(SettingsActivity.DOWNLOAD_ORIGIN, b).apply()
        }
        binding.previewRes.setOnCheckedChangeListener { _, b ->
            preferences.edit().putBoolean(SettingsActivity.PREVIEW_RES, b).apply()
        }
        binding.darkSwitch.setOnCheckedChangeListener { _, b ->
            dark = b
            stateChanged = true
            preferences.edit().putBoolean(SettingsActivity.DARK, dark).apply()
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                (activity as AppCompatActivity).delegate.applyDayNight()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                (activity as AppCompatActivity).delegate.applyDayNight()
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
        when (preferences.getInt(SettingsActivity.RANDOM_LOCATION, 2)) {
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

        val dimensions = AppUtils.getWallDimensions(context)
        binding.widthEdit.setText(dimensions[0].toString() + "")
        binding.heightEdit.setText(dimensions[1].toString() + "")

        val defaultSub = preferences.getString(SettingsActivity.DEFAULT, "mobilewallpaper")
        binding.defaultEdit.setText(defaultSub)

        binding.saveButton.setOnClickListener {
            saveSettings()
        }

        binding.randomLocationButton.setOnClickListener {
            val but = binding.randomLocationButton
            val builder = MaterialAlertDialogBuilder(context, R.style.MyThemeOverlayAlertDialog)
            builder.setTitle("Set Where?")
                    .setItems(R.array.location_options) { _, i ->
                        when (i) {
                            0 -> {
                                but.text = "HOME"
                                preferences.edit()?.putInt(SettingsActivity.RANDOM_LOCATION, SettingsActivity.HOME)?.apply()
                            }

                            1 -> {
                                but.text = "LOCK"
                                preferences.edit()?.putInt(SettingsActivity.RANDOM_LOCATION, SettingsActivity.LOCK)?.apply()
                            }

                            2 -> {
                                but.text = "BOTH"
                                preferences.edit()?.putInt(SettingsActivity.RANDOM_LOCATION, SettingsActivity.BOTH)?.apply()
                            }

                            else -> {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            builder.create().show()
        }
    }

    private fun showRandomSettings(show: Boolean) {
        val setting = if (show) View.VISIBLE else View.GONE
        binding.refreshLocationSetting.visibility = setting
        binding.randomSeekSection.visibility = setting
        binding.changeIntervalTitle.visibility = setting
    }

    private fun saveSettings() {
        val context = requireContext()
        var valid = true
        val preferenceEditor = preferences.edit()
        //preferenceEditor.putInt(LOAD_SCALE, binding.scaleSeek.progress)
        preferenceEditor.putInt(SettingsActivity.RANDOM_INTERVAL, binding.intervalSeek.progress)
        val setRefresh = preferences.getBoolean(SettingsActivity.RANDOM_ENABLED, false) && alarmChanged

        if (setRefresh) {
            val interval = (binding.intervalSeek.progress + 1) * 60 * 60 * 1000
            val triggerTime = SystemClock.elapsedRealtime() + interval
            if (wallAlarm != null) {
                Toast.makeText(context, "Random refresh enabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, interval.toLong(), pending)
            }
        } else {
            if (wallAlarm != null && !preferences.getBoolean(SettingsActivity.RANDOM_ENABLED, false)) {
                Toast.makeText(context, "Random refresh disabled", Toast.LENGTH_SHORT).show()
                wallAlarm!!.cancel(pending!!)
            }
        }
        val dims = AppUtils.getWallDimensions(context)
        var width = dims[0]
        var height = dims[1]
        try {
            width = Integer.parseInt(binding.widthEdit.text.toString())
            height = Integer.parseInt(binding.heightEdit.text.toString())
        } catch (e: NumberFormatException) {
            valid = false
            Toast.makeText(context, "You didn't enter a valid number", Toast.LENGTH_SHORT).show()
        }

        if (valid) {
            preferenceEditor.putInt(SettingsActivity.IMG_WIDTH, width)
            preferenceEditor.putInt(SettingsActivity.IMG_HEIGHT, height)
        }

        val defaultSub = binding.defaultEdit.text.toString().replace(" ", "")
        preferenceEditor.putString(SettingsActivity.DEFAULT, defaultSub)
        preferenceEditor.apply()
        Toast.makeText(context, "Saved Settings", Toast.LENGTH_SHORT).show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

