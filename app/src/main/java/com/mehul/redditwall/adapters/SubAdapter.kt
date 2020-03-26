package com.mehul.redditwall.adapters

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.savedsub.SubSaved

class SubAdapter(private val con: Context) : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(con)
    private var subs: List<SubSaved?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val itemView = inflater.inflate(R.layout.sub_saved_item, parent, false)
        return SubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        if (subs != null) {
            val current = subs!![position]
            holder.itemView.apply {
                isLongClickable = true
                isClickable = true
            }
            holder.bindTo(current)
            holder.itemView.setOnLongClickListener {
                val defaultConfirm = MaterialAlertDialogBuilder(con, R.style.MyThemeOverlayAlertDialog).apply {
                    setTitle("Set as default?")
                    setMessage("Are you sure you want " + current?.subName + " to be your default subreddit?")
                    setPositiveButton("Yes") { _, _ ->
                        val preferences = con.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                        val prefEdit = preferences.edit()
                        prefEdit.putString(SettingsActivity.DEFAULT, current?.subName)
                        prefEdit.apply()
                        Toast.makeText(con, "Set " + current?.subName + " as default", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No") { _, _ -> Toast.makeText(con, "Cancelled", Toast.LENGTH_SHORT).show() }
                }
                defaultConfirm.show()
                true
            }
            holder.itemView.setOnClickListener {
                val launchMain = Intent(con, MainActivity::class.java)
                launchMain.apply {
                    putExtra(MainActivity.SAVED, current?.subName)
                    putExtra(MainActivity.OVERRIDE, true)
                }
                val clipboard: ClipboardManager? = con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", current?.subName)
                assert(clipboard != null)
                clipboard?.setPrimaryClip(clip)
                Toast.makeText(con, "Saved to clipboard", Toast.LENGTH_SHORT).show()
                con.startActivity(launchMain)
            }
        }
    }

    fun setSubs(subs: List<SubSaved?>?) {
        this.subs = subs
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (subs != null)
            subs!!.size
        else
            0
    }

    fun getSubAtPosition(position: Int): SubSaved? {
        return subs!![position]
    }

    inner class SubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subTv: TextView = itemView.findViewById(R.id.sub_name)
        private val dateTv: TextView = itemView.findViewById(R.id.sub_date)

        @SuppressLint("SetTextI18n")
        fun bindTo(saved: SubSaved?) {
            val date = saved!!.subDate
            val name = saved.subName

            subTv.text = name
            dateTv.text = "Saved on $date"
        }
    }
}
