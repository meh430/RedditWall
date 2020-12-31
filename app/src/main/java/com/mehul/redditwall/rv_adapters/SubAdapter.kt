package com.mehul.redditwall.rv_adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehul.redditwall.R
import com.mehul.redditwall.objects.Subreddit
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SubAdapter
    : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {
    var subreddits: ArrayList<Subreddit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sub_card, parent, false)
        return SubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val current = subreddits[position]
        holder.bindTo(current)
    }

    fun updateSubreddits(subs: ArrayList<Subreddit>) {
        subreddits = subs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return subreddits.size
    }


    inner class SubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subTv: TextView = itemView.findViewById(R.id.subName)
        private val icon: ImageView = itemView.findViewById(R.id.subIcon)
        private val subNumTv: TextView = itemView.findViewById(R.id.subSubs)
        private val subDescTv: TextView = itemView.findViewById(R.id.subDesc)
        private val dateTv: TextView = itemView.findViewById(R.id.subDate)

        @SuppressLint("SetTextI18n")
        fun bindTo(sub: Subreddit) {
            dateTv.visibility = View.GONE
            subTv.text = sub.subName
            subNumTv.text = "Subscribers: ${NumberFormat.getNumberInstance(Locale.US).format(sub.subscribers)}"
            subDescTv.text = sub.subDesc
            icon.clipToOutline = true
            if (sub.subIcon.isEmpty() || sub.subIcon.isBlank()) {
                val def = ContextCompat.getDrawable(itemView.context.applicationContext, R.drawable.ic_android)
                def?.setTint(Color.BLACK)
                icon.setImageDrawable(def)
            } else {
                Glide.with(icon.context).load(sub.subIcon).override(200, 200).centerCrop().into(icon)
            }
        }
    }
}
