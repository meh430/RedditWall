package com.mehul.redditwall.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehul.redditwall.R
import com.mehul.redditwall.objects.HistoryItem

class HistAdapter(private val con: Context) : RecyclerView.Adapter<HistAdapter.HistViewHolder>() {
    private val inflater = LayoutInflater.from(con)
    private var histories: List<HistoryItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistViewHolder {
        val itemView = inflater.inflate(R.layout.history_item, parent, false)
        return HistViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    fun setHistories(hists: List<HistoryItem>) {
        this.histories = hists
        notifyDataSetChanged()
    }

    fun getHistory(position: Int): HistoryItem {
        return histories[position]
    }

    override fun onBindViewHolder(holder: HistViewHolder, position: Int) {
        val current = histories[position]
        holder.bindTo(current)
    }

    inner class HistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv: TextView = itemView.findViewById(R.id.hist_name)
        private val sourceTv: TextView = itemView.findViewById(R.id.down_set)
        private val dateTv: TextView = itemView.findViewById(R.id.history_date)
        private val img: ImageView = itemView.findViewById(R.id.history_image)

        fun bindTo(history: HistoryItem) {
            val name = history.subName
            val date = history.setDate
            val source = HistoryItem.sources[history.source]

            nameTv.text = name
            sourceTv.text = source
            dateTv.text = date
            Glide.with(con).load(history.url).override(210, 350).into(img)
        }
    }
}