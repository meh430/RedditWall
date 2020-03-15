package com.mehul.redditwall

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    private var json = ""
    private var histJob: Job? = null
    private var adapter: HistAdapter? = null
    private var histViewModel: HistViewModel? = null
    private var images: ArrayList<Bitmap?> = ArrayList()
    private var histories: List<HistoryItem?> = ArrayList()
    private var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        loading = findViewById(R.id.hist_loading)
        histViewModel = ViewModelProvider(this@HistoryActivity).get(HistViewModel(application)::class.java)
        val recycler = findViewById<RecyclerView>(R.id.hist_scroll)
        adapter = HistAdapter(this)
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
                        val saved = adapter!!.getHistory(position)
                        histViewModel!!.deleteHist(saved)
                        adapter!!.notifyDataSetChanged()
                    }
                })

        helper.attachToRecyclerView(recycler)
        histViewModel = ViewModelProvider(this).get(HistViewModel::class.java)
        val con = this
        histViewModel!!.allHist!!.observe(this, Observer { histories ->
            this.histories = histories!!

            val hists = histories.sortedWith(compareBy
            { SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss", Locale.CANADA).parse(it!!.internalDate) }).asReversed()

            loading?.visibility = View.VISIBLE

            histJob = uiScope.launch {
                loadImages(hists, con)
            }
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
                    confirmDelete.setMessage("Do you want to clear your favorites?")
                    confirmDelete.setPositiveButton("Yes") { _, _ ->
                        histViewModel!!.deleteAll()
                        Toast.makeText(this@HistoryActivity, "Deleted favorite images", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.setNegativeButton("No") { _, _ ->
                        Toast.makeText(this@HistoryActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    confirmDelete.show()
                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.down_all -> {
                    Toast.makeText(con, "TODO", Toast.LENGTH_SHORT).show()
                    return@OnActionSelectedListener false // false will close it without animation
                }
                R.id.random -> {
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

    override fun onResume() {
        super.onResume()
        val con = this
        if (histories.size != images.size) {
            histJob = uiScope.launch {
                loadImages(histories, con)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        val con = this
        if (histories.size != images.size) {
            histJob = uiScope.launch {
                loadImages(histories, con)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun loadImages(hists: List<HistoryItem?>, con: Context) {
        loading?.visibility = View.VISIBLE
        withContext(Dispatchers.IO) {
            for (hist in hists) {
                val bitmap = Glide.with(con).asBitmap().load(hist!!.url)
                        .override(200, 325).centerCrop().submit().get()
                withContext(Dispatchers.Main) {
                    images.add(bitmap)
                }
            }

            withContext(Dispatchers.Default) {
                json = convertToJSON(hists)
            }
        }
        adapter!!.setHistories(hists)
        adapter!!.setImages(images)
        loading?.visibility = View.GONE
        findViewById<View>(R.id.hist_empty).visibility = if (adapter!!.itemCount == 0) View.VISIBLE else View.GONE
    }

    private suspend fun convertToJSON(hists: List<HistoryItem?>): String {
        var json = ""
        withContext(Dispatchers.Default) {
            val bits = ArrayList<BitURL>()
            for (i in hists.indices) {
                bits.add(BitURL(images[i], hists[i]!!.url, hists[i]!!.postLink))
            }
            json = Gson().toJson(bits)
        }
        return json
    }
}
