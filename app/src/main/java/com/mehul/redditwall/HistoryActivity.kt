package com.mehul.redditwall

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
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
import com.google.gson.Gson
import com.leinardi.android.speeddial.SpeedDialView
import com.mehul.redditwall.history.HistAdapter
import com.mehul.redditwall.history.HistViewModel
import com.mehul.redditwall.history.HistoryItem
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var currSort = R.id.recent
    private var json = ""
    private var histJob: Job? = null
    private var adapter: HistAdapter? = null
    private var histViewModel: HistViewModel? = null
    private var histories: List<HistoryItem?> = ArrayList()
    private var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val sortIcon: Drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_sort)!!
        val dark = getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                .getBoolean(SettingsActivity.DARK, false)
        if (dark) {
            sortIcon.setTint(Color.WHITE)
        } else {
            sortIcon.setTint(Color.BLACK)
        }

        toolbar.overflowIcon = sortIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        loading = findViewById(R.id.hist_loading)
        histViewModel = ViewModelProvider(this@HistoryActivity).get(HistViewModel(application)::class.java)
        val recycler = findViewById<RecyclerView>(R.id.hist_scroll)
        adapter = HistAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        adapter!!.setHistories(histories)
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
                        val saved = adapter!!.getHistory(position)
                        histViewModel!!.deleteHist(saved)
                        histories = histViewModel!!.allHist!!.value!!
                    }
                })

        helper.attachToRecyclerView(recycler)
        histViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        val con = this
        histViewModel!!.allHist!!.observe(this, Observer { hists ->

            this.histories = when (currSort) {
                R.id.alpha -> {
                    hists!!.sortedWith(compareBy { it?.subName }).asReversed()
                }
                R.id.oldest -> {
                    hists!!.sortedWith(compareBy
                    { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })

                }
                else -> {
                    hists!!.sortedWith(compareBy
                    { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) }).asReversed()
                }
            }
            adapter!!.setHistories(histories)

            uiScope.launch {
                json = convertToJSON(histories)
            }
            findViewById<View>(R.id.hist_empty).visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
        })
        val currCon = this
        recycler.addOnItemTouchListener(RecyclerListener(this, recycler, object : RecyclerListener.OnItemClickListener {
            override fun onLongItemClick(view: View?, position: Int) {}

            override fun onItemClick(view: View, position: Int) {
                cancelThreads()
                val current = histories[position]
                val wallIntent = Intent(currCon, WallActivity::class.java)
                wallIntent.apply {
                    putExtra(WallActivity.WALL_URL, current?.url)
                    putExtra(WallActivity.GIF, false)
                    putExtra(WallActivity.FROM_FAV, false)
                }

                wallIntent.putExtra(WallActivity.INDEX, position)
                wallIntent.putExtra(WallActivity.LIST, json)
                Log.e("JSON", json)

                currCon.startActivity(wallIntent)
            }
        }))

        val speedView = findViewById<SpeedDialView>(R.id.speedDial)
        speedView.inflate(R.menu.fab_menu)
        speedView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.delete_all -> {
                    val confirmDelete = AlertDialog.Builder(this)
                    confirmDelete.setTitle("Are you sure?")
                    confirmDelete.setMessage("Do you want to clear history?")
                    confirmDelete.setPositiveButton("Yes") { _, _ ->
                        histViewModel!!.deleteAll()
                        Toast.makeText(this@HistoryActivity, "Cleared history", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.setNegativeButton("No") { _, _ ->
                        Toast.makeText(this@HistoryActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.show()
                    return@OnActionSelectedListener false
                }
                R.id.down_all -> {
                    Toast.makeText(con, "TODO", Toast.LENGTH_SHORT).show()
                    return@OnActionSelectedListener false
                }
                R.id.random -> {
                    if (histories.isEmpty()) {
                        Toast.makeText(con, "No items", Toast.LENGTH_SHORT).show()
                        return@OnActionSelectedListener false
                    }


                    val randomNum = (0..histories.size).random()
                    cancelThreads()
                    val current = histories[randomNum]
                    val wallIntent = Intent(currCon, WallActivity::class.java)
                    wallIntent.apply {
                        putExtra(WallActivity.WALL_URL, current?.url)
                        putExtra(WallActivity.GIF, false)
                        putExtra(WallActivity.FROM_FAV, false)
                    }
                    wallIntent.putExtra(WallActivity.INDEX, randomNum)
                    wallIntent.putExtra(WallActivity.LIST, json)
                    Log.e("JSON", json)

                    currCon.startActivity(wallIntent)
                    return@OnActionSelectedListener false
                }
            }

            false
        })
    }

    private fun cancelThreads() {
        histJob?.cancel()
    }

    override fun onStop() {
        super.onStop()
        cancelThreads()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelThreads()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        currSort = item.itemId
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            R.id.alpha -> {
                histories = histories.sortedWith(compareBy { it?.subName })
            }
            R.id.recent -> {
                histories = histories.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) }).asReversed()

            }
            R.id.oldest -> {
                histories = histories.sortedWith(compareBy
                { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) })
            }
        }

        if (item.itemId != android.R.id.home) {
            adapter?.setHistories(histories)
            uiScope.launch {
                json = convertToJSON(histories)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private suspend fun convertToJSON(hists: List<HistoryItem?>): String {
        var json = ""
        withContext(Dispatchers.Default) {
            val bits = ArrayList<BitURL>()
            for (i in hists) {
                val temp = BitURL(null, i!!.url, i.postLink)
                temp.setGif(false)
                bits.add(temp)
            }
            json = Gson().toJson(bits)
        }
        return json
    }
}
