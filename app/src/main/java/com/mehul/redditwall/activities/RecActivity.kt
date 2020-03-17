package com.mehul.redditwall.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehul.redditwall.R
import com.mehul.redditwall.adapters.RecAdapter
import com.mehul.redditwall.objects.Recommendation
import com.mehul.redditwall.rest.RestRecs
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

//TODO: manage threads properly
class RecActivity : AppCompatActivity() {
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var recJob: Job? = null
    private var loading: ProgressBar? = null
    private var recycler: RecyclerView? = null
    private var error: TextView? = null
    private var adapter: RecAdapter? = null
    private var recs = ArrayList<Recommendation>()
    private val cancelThreads = {
        if (recJob != null) {
            recJob?.cancel()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec)
        supportActionBar?.elevation = 0F
        loading = findViewById(R.id.rec_loading)
        recycler = findViewById(R.id.rec_scroll)
        error = findViewById(R.id.rec_empty)
        error?.visibility = View.INVISIBLE
        adapter = RecAdapter(this, recs)
        recycler?.adapter = adapter
        recycler?.layoutManager = LinearLayoutManager(this)
        recJob = uiScope.launch {
            loadRecs()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelThreads()
    }

    override fun onStop() {
        super.onStop()
        cancelThreads()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun loadRecs() {
        withContext(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                error?.visibility = View.GONE
                loading?.visibility = View.VISIBLE
            }

            val json = RestRecs.getRecsJSON()

            val res = if (json.isEmpty()) ArrayList() else RestRecs.parseJSON(json)

            withContext(Dispatchers.Main) {
                loading?.visibility = View.GONE
                if (res.size != 0) {
                    recs.addAll(res)
                    recs.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
                    adapter?.notifyDataSetChanged()
                } else {
                    error?.visibility = View.VISIBLE
                }
            }
        }
    }
}
