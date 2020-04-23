package com.mehul.redditwall.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.objects.Subreddit
import com.mehul.redditwall.viewmodels.SubViewModel
import kotlinx.android.synthetic.main.sub_card.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SubAdapter(private val con: Context, private val vm: SubViewModel)
    : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(con)
    private var subs: List<Subreddit> = ArrayList()
    private var saves: List<Subreddit> = ArrayList()

    init {
        vm.allSubs.observe(con as LifecycleOwner, androidx.lifecycle.Observer { savedSubs ->
            saves = savedSubs
        })
    }

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
        holder.itemView.subOptions.setOnClickListener {
            val conWrap = ContextThemeWrapper(con, R.style.AppTheme_PopupOverlay)
            val popMenu = PopupMenu(conWrap, holder.itemView.subOptions)
            popMenu.inflate(R.menu.subreddit_menu)
            val saved = alreadySaved(current.subName)
            popMenu.menu.findItem(R.id.saveSubreddit).title = if (saved) "Unsave" else "Save"
            popMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.saveSubreddit -> {
                        val currState = item.title.toString()
                        if (currState.equals("save", true)) {
                            val save = Subreddit((0..999999999).random(), current.subName,
                                    current.subDesc, current.subscribers, current.subIcon,
                                    SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss", Locale.CANADA).format(Date()))
                            vm.insert(save)
                            item.title = "Unsave"
                        } else {
                            val delete = findSaved(current.subName)
                            vm.deleteSubreddit(delete!!)
                            item.title = "Save"
                        }
                        true
                    }

                    R.id.setDefault -> {
                        val defaultConfirm =
                                MaterialAlertDialogBuilder(con, R.style.MyThemeOverlayAlertDialog).apply {
                                    setTitle("Set as default?")
                                    setMessage("Are you sure you want " + current.subName + " to be your default subreddit?")
                                    setPositiveButton("Yes") { _, _ ->
                                        val preferences =
                                                con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                                        val prefEdit = preferences.edit()
                                        prefEdit.putString(SettingsActivity.DEFAULT, current.subName)
                                        prefEdit.apply()
                                        Toast.makeText(con, "Set " + current.subName + " as default", Toast.LENGTH_SHORT).show()
                                    }
                                    setNegativeButton("No") { _, _ ->
                                        Toast.makeText(con, "Cancelled", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        defaultConfirm.show()
                        true
                    }

                    else -> false
                }
            }
            popMenu.show()
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

    fun alreadySaved(name: String): Boolean {
        for (saved in saves) {
            if (saved.subName.equals(name, true)) {
                return true
            }
        }
        return false
    }

    fun findSaved(name: String): Subreddit? {
        for (saved in saves) {
            if (saved.subName.equals(name, true)) {
                return saved
            }
        }
        return null
    }

    inner class SubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subTv: TextView = itemView.findViewById(R.id.subName)
        private val icon: ImageView = itemView.findViewById(R.id.subIcon)
        private val subNumTv: TextView = itemView.findViewById(R.id.subSubs)
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
