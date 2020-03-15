package com.mehul.redditwall

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehul.redditwall.savedsub.SubAdapter
import com.mehul.redditwall.savedsub.SubSaved
import com.mehul.redditwall.savedsub.SubViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SavedActivity : AppCompatActivity() {
    private var currSort = R.id.recent
    private var adapter: SubAdapter? = null
    private var saveText: EditText? = null
    private var subs: List<SubSaved?>? = ArrayList()
    private var subViewModel: SubViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val sortIcon: Drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_sort)!!
        toolbar.overflowIcon = sortIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        subViewModel = ViewModelProvider(this@SavedActivity).get(SubViewModel(application)::class.java)
        val recycler = findViewById<RecyclerView>(R.id.saveScroll)
        saveText = findViewById(R.id.search)
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
        subViewModel = ViewModelProvider(this).get(SubViewModel::class.java)
        subViewModel!!.allSubs!!.observe(this, Observer { subSaveds ->
            subs = when (currSort) {
                R.id.oldest -> {
                    subSaveds?.sortedWith(compareBy
                    { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })
                }
                R.id.alpha -> {
                    subSaveds?.sortedWith(compareBy { it?.subName })
                }
                else -> {
                    subSaveds?.sortedWith(compareBy
                    { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })?.asReversed()
                }
            }
            adapter!!.setSubs(subs)

            findViewById<View>(R.id.sub_empty).visibility = if (adapter!!.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        findViewById<View>(R.id.sub_empty).visibility = if (adapter!!.itemCount == 0) {
            View.VISIBLE
        } else {
            View.GONE
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
                val confirmSubs = AlertDialog.Builder(this)
                confirmSubs.setTitle("Are you sure?")
                confirmSubs.setMessage("Do you want to clear your saved subreddits?")
                confirmSubs.setPositiveButton("Yes") { _, _ ->
                    subViewModel!!.deleteAll()
                    Toast.makeText(this@SavedActivity, "Deleted saved subs", Toast.LENGTH_SHORT).show()
                }
                confirmSubs.setNegativeButton("No") { _, _ -> Toast.makeText(this@SavedActivity, "Cancelled", Toast.LENGTH_SHORT).show() }
                confirmSubs.show()
                return true
            }
            R.id.recent -> {
                subs = subs?.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })?.asReversed()
                adapter!!.setSubs(subs)
            }
            R.id.alpha -> {
                subs = subs?.sortedWith(compareBy { it?.subName })
                adapter!!.setSubs(subs)
            }
            R.id.oldest -> {
                subs = subs?.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })
                adapter!!.setSubs(subs)
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
        var saveVal = saveText!!.text.toString()
        saveVal = if (saveVal.length == 1) {
            Toast.makeText(this, "Please enter something to save", Toast.LENGTH_SHORT).show()
            return
        } else {
            saveText!!.setText("")
            saveVal.toLowerCase(Locale.ROOT)
        }
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
        for (saved in subViewModel!!.allSubs!!.value!!) {
            if (saved?.subName.equals(saveVal, ignoreCase = true)) {
                Toast.makeText(this, saved!!.subName + " has already been saved", Toast.LENGTH_SHORT).show()
                return
            }
        }
        subViewModel!!.insert(SubSaved((Math.random() * 10000).toInt() + 1, saveVal, SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).format(Date())))
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }
}