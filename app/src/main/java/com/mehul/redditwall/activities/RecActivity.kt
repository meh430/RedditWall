package com.mehul.redditwall.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mehul.redditwall.adapters.RecAdapter
import com.mehul.redditwall.databinding.ActivityRecBinding
import com.mehul.redditwall.objects.Recommendation
import com.mehul.redditwall.rest.RestRecs
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class RecActivity : AppCompatActivity() {
    private var uiScope = CoroutineScope(Dispatchers.Main)
    private var recJob: Job? = null
    private var adapter: RecAdapter? = null
    private var recs = ArrayList<Recommendation>()
    private lateinit var binding: ActivityRecBinding

    private val cancelThreads = {
        if (recJob != null) {
            recJob?.cancel()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0F
        adapter = RecAdapter(this, recs)
        binding.recEmpty.visibility = View.INVISIBLE
        binding.recScroll.adapter = adapter
        binding.recScroll.layoutManager = LinearLayoutManager(this)
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
                binding.recEmpty.visibility = View.GONE
                binding.recLoading.visibility = View.VISIBLE
            }

            val json = RestRecs.getRecsJSON()

            val res = if (json.isEmpty()) ArrayList() else RestRecs.parseJSON(json)

            withContext(Dispatchers.Main) {
                binding.recLoading.visibility = View.GONE
                if (res.size != 0) {
                    recs.addAll(res)
                    recs.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
                    adapter?.notifyDataSetChanged()
                } else {
                    binding.recEmpty.visibility = View.VISIBLE
                }
            }
        }
    }
}
