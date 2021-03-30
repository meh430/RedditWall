@file:Suppress("DEPRECATION")

package com.mehul.redditwall.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivityMainBinding
import kotlinx.coroutines.InternalCoroutinesApi

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private var preferences: SharedPreferences? = null

    private lateinit var binding: ActivityMainBinding

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0.0F

        preferences = getSharedPreferences(SharedPrefFile, Context.MODE_PRIVATE)

        val dark = preferences!!.getBoolean(SettingsActivity.DARK, false)
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragNavHost)
        binding.bottomNavView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
                topLevelDestinationIds = setOf(
                        R.id.homeFragment,
                        R.id.favsFragment,
                        R.id.searchFragment,
                        R.id.historyFragment,
                        R.id.settingsFragment
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("DESTROY", "main")

    }

    companion object {
        const val SharedPrefFile = "com.mehul.redditwall"
    }
}