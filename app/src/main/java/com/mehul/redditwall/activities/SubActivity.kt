package com.mehul.redditwall.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mehul.redditwall.R
import com.mehul.redditwall.databinding.ActivitySubBinding
import com.mehul.redditwall.fragments.SavedSubsFragment
import com.mehul.redditwall.fragments.SearchSubsFragment
import com.mehul.redditwall.viewmodels.SubViewModel

class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding
    private lateinit var searchFrag: SearchSubsFragment
    private lateinit var savedFrag: SavedSubsFragment
    private lateinit var fManager: FragmentManager
    private lateinit var currFrag: Fragment
    private lateinit var subViewModel: SubViewModel
    private lateinit var sortChoice: Sorting

    private val bottomBarEvents = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.searchSubs -> {
                supportActionBar?.title = "Search Subreddits"
                fManager.beginTransaction().hide(currFrag).show(searchFrag).commit()
                currFrag = searchFrag
                return@OnNavigationItemSelectedListener true
            }
            R.id.savedSubs -> {
                supportActionBar?.title = "Saved Subreddits"
                fManager.beginTransaction().hide(currFrag).show(savedFrag).commit()
                currFrag = savedFrag
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subViewModel = ViewModelProvider(this).get(SubViewModel::class.java)

        searchFrag = SearchSubsFragment.newInstance()
        savedFrag = SavedSubsFragment.newInstance()

        sortChoice = savedFrag as Sorting

        currFrag = searchFrag
        fManager = supportFragmentManager
        fManager.beginTransaction().add(R.id.fragment_holder, savedFrag, "save").hide(savedFrag).commit()
        fManager.beginTransaction().add(R.id.fragment_holder, searchFrag, "search").commit()
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
        binding.bottomNav.setOnNavigationItemSelectedListener(bottomBarEvents)
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
                val confirmSubs = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlayAlertDialog).apply {
                    setTitle("Are You Sure?")
                    setMessage("Do you want to clear all saved subreddits?")
                    setPositiveButton("Yes") { _, _ ->
                        subViewModel.deleteAll()
                        //Toast.makeText(this@SavedActivity, "Deleted saved subs", Toast.LENGTH_SHORT).show()
                        Snackbar.make(binding.root, "Deleted saved subs", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No") { _, _ ->
                        Snackbar.make(binding.root, "Cancelled", Snackbar.LENGTH_SHORT).show()
                    }
                }
                confirmSubs.show()
                return true
            }
            else -> {
                sortChoice.sortOrder(item.itemId)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public interface Sorting {
        fun sortOrder(sort: Int)
    }
}
