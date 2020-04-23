package com.mehul.redditwall.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.objects.Subreddit
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SubAdapter(private val con: Context) : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(con)
    private var subs: List<Subreddit> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val itemView = inflater.inflate(R.layout.sub_card, parent, false)
        return SubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val current = subs[position]
        holder.itemView.apply {
            isLongClickable = true
            isClickable = true
        }
        holder.bindTo(current)
        holder.itemView.setOnClickListener {
            val subName = current.subName.replace("r/", "")
            val launchMain = Intent(con, MainActivity::class.java)
            launchMain.apply {
                putExtra(MainActivity.SAVED, subName)
                putExtra(MainActivity.OVERRIDE, true)
            }
            val clipboard: ClipboardManager? = con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", subName)
            assert(clipboard != null)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(con, "Saved to clipboard", Toast.LENGTH_SHORT).show()
            con.startActivity(launchMain)
            (con as Activity).finish()
        }
    }

    fun setSubs(subs: List<Subreddit>) {
        this.subs = subs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return subs.size
    }

    fun getSubAtPosition(position: Int): Subreddit {
        return subs[position]
    }

    inner class SubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subTv: TextView = itemView.findViewById(R.id.subName)
        private val icon: ImageView = itemView.findViewById(R.id.subIcon)
        private val subNumTv: TextView = itemView.findViewById(R.id.subSubs)
        private val options: TextView = itemView.findViewById(R.id.subOptions)
        private val subDescTv: TextView = itemView.findViewById(R.id.subDesc)
        private val dateTv: TextView = itemView.findViewById(R.id.subDate)

        @SuppressLint("SetTextI18n")
        fun bindTo(sub: Subreddit) {
            if (sub.subDate == null) {
                dateTv.visibility = View.GONE
            } else {
                dateTv.visibility = View.VISIBLE
                dateTv.text = "Saved on ${sub.subDate}"
            }

            subTv.text = sub.subName
            subNumTv.text = "Subscribers: ${NumberFormat.getNumberInstance(Locale.US).format(sub.subscribers)}"
            subDescTv.text = sub.subDesc
            if (sub.subIcon.isEmpty() || sub.subIcon.isBlank()) {
                val def = ContextCompat.getDrawable(con.applicationContext, R.drawable.ic_android)
                def?.setTint(Color.BLACK)
                icon.setImageDrawable(def)
            } else {
                Glide.with(con).load(sub.subIcon).override(200, 200).into(icon)
            }
        }
    }
}
