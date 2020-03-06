package com.mehul.redditwall

import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference
import java.util.*

//TODO: manage threads properly
class RecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec)
        val loading = findViewById<ProgressBar>(R.id.rec_loading)
        val recycler = findViewById<RecyclerView>(R.id.rec_scroll)
        val error = findViewById<TextView>(R.id.rec_empty)
        error.visibility = View.INVISIBLE
        val recs = ArrayList<Recommendation>()
        val adapter = RecAdapter(this, recs)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        val task = LoadRecTask(loading, recs, adapter, error)
        task.execute()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    internal class LoadRecTask(
            l: ProgressBar, r: ArrayList<Recommendation>,
            a: RecAdapter, e: TextView) : AsyncTask<Void, Void, ArrayList<Recommendation>>() {
        private var load: WeakReference<ProgressBar> = WeakReference(l)
        private var rList: WeakReference<ArrayList<Recommendation>> = WeakReference(r)
        private var adapt: WeakReference<RecAdapter> = WeakReference(a)
        private var err: WeakReference<TextView> = WeakReference(e)

        override fun onPreExecute() {
            err.get()?.visibility = View.GONE
            load.get()?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg voids: Void): ArrayList<Recommendation> {
            return RestRecs.parseJSON(RestRecs.recsJSON!!)
        }

        override fun onPostExecute(res: ArrayList<Recommendation>) {
            load.get()?.visibility = View.GONE
            if (res.size != 0) {
                rList.get()?.addAll(res)
                rList.get()?.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
                adapt.get()?.notifyDataSetChanged()
            } else {
                err.get()?.visibility = View.VISIBLE
            }
        }
    }
}
