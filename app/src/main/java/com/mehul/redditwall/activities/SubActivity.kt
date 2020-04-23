package com.mehul.redditwall.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivitySubBinding
import com.mehul.redditwall.fragments.SearchSubsFragment

class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding

    private val bottomBarEvents = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.searchSubs -> {
                inflateFragment(SearchSubsFragment.newInstance(), "Search Subreddits")
                return@OnNavigationItemSelectedListener true
            }
            R.id.savedSubs -> {
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Search Subreddits"
        val sortIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_sort)
        val dark = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.DARK, false)
        sortIcon?.setTint(if (dark) Color.WHITE else Color.BLACK)
        binding.toolbar.overflowIcon = sortIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        inflateFragment(SearchSubsFragment.newInstance(), "Search Subreddits")
        binding.bottomNav.setOnNavigationItemSelectedListener(bottomBarEvents)
    }

    private fun inflateFragment(fragment: Fragment, title: String) {
        val transaction = supportFragmentManager.beginTransaction()
        supportActionBar?.title = title
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
    }


    //menu stuff
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        menu.add(Menu.NONE, 56345564, Menu.NONE, "Clear Saved")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //currSort = item.itemId
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            56345564 -> {
                /*val confirmSubs = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlayAlertDialog).apply {
                    setTitle("Are You Sure?")
                    setMessage("Do you want to clear ${subs.size} saved subreddits?")
                    setPositiveButton("Yes") { _, _ ->
                        subViewModel!!.deleteAll()
                        //Toast.makeText(this@SavedActivity, "Deleted saved subs", Toast.LENGTH_SHORT).show()
                        Snackbar.make(binding.root, "Deleted saved subs", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No") { _, _ ->
                        Snackbar.make(binding.root, "Cancelled", Snackbar.LENGTH_SHORT).show()
                    }
                }
                confirmSubs.show()
                return true*/
                return true
            }
            else -> {
                //subs = sortList(currSort, subs)
                //adapter?.setSubs(subs)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
