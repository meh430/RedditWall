package com.mehul.redditwall.activities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.SubAdapter
import com.mehul.redditwall.databinding.ActivitySavedBinding
import com.mehul.redditwall.objects.SubSaved
import com.mehul.redditwall.viewmodels.SubViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SavedActivity : AppCompatActivity() {
    private var currSort = R.id.recent
    private var adapter: SubAdapter? = null
    private var subs: List<SubSaved> = ArrayList()
    private var subViewModel: SubViewModel? = null
    private lateinit var binding: ActivitySavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val sortIcon: Drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_sort)!!
        val dark = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.DARK, false)
        sortIcon.setTint(if (dark) Color.WHITE else Color.BLACK)
        toolbar.overflowIcon = sortIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        subViewModel = ViewModelProvider(this@SavedActivity).get(SubViewModel(application)::class.java)
        val recycler = binding.saveScroll
        adapter = SubAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val saved = adapter!!.getSubAtPosition(position)
                        subViewModel!!.deleteSavedSub(saved)
                        adapter!!.notifyDataSetChanged()
                    }
                })

        helper.attachToRecyclerView(recycler)
        subViewModel!!.allSubs.observe(this, Observer { subSaveds ->
            subs = sortList(currSort, subSaveds!!)
            adapter!!.setSubs(subs)
            binding.subEmpty.visibility = if (adapter!!.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun sortList(sort: Int, list: List<SubSaved>): List<SubSaved> {
        return when (sort) {
            R.id.recent -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it.internalDate) }).asReversed()
            }
            R.id.alpha -> {
                list.sortedWith(compareBy { it.subName })
            }
            else -> {
                list.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).parse(it.internalDate) })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currSort = item.itemId
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            56345564 -> {
                val confirmSubs = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlayAlertDialog).apply {
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
                return true
            }
            else -> {
                subs = sortList(currSort, subs)
                adapter?.setSubs(subs)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        menu.add(Menu.NONE, 56345564, Menu.NONE, "Clear Saved")
        return true
    }

    fun saveSub(view: View) {
        var saveVal = binding.search.text.toString().replace(" ", "")
        saveVal = if (saveVal.length == 1) {
            //Toast.makeText(this, "Please enter something to save", Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Please enter something to save", Snackbar.LENGTH_SHORT).show()
            return
        } else {
            binding.search.setText("")
            saveVal.toLowerCase(Locale.ROOT)
        }
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        for (saved in subViewModel!!.allSubs.value!!) {
            if (saved.subName.equals(saveVal, ignoreCase = true)) {
                //Toast.makeText(this, saved!!.subName + " has already been saved", Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.root, "${saved.subName} has already been saved", Snackbar.LENGTH_SHORT).show()
                return
            }
        }
        subViewModel!!.insert(SubSaved((Math.random() * 10000).toInt() + 1,
                saveVal, SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date())))
        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        Snackbar.make(binding.root, "Saved", Snackbar.LENGTH_SHORT).show()
    }
}