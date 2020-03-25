package com.mehul.redditwall.adapters

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehul.redditwall.R
import com.mehul.redditwall.activities.MainActivity
import com.mehul.redditwall.activities.SettingsActivity
import com.mehul.redditwall.objects.Recommendation
import java.util.*

class RecAdapter internal constructor(private val context: Context,
                                      private val recs: ArrayList<Recommendation>?) : RecyclerView.Adapter<RecAdapter.ReccViewHolder>() {
    private val inflater: LayoutInflater
    private val width: Int
    private val height: Int

    init {
        val dims = MainActivity.getDimensions(context)
        width = dims[0]
        height = dims[1]
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReccViewHolder {
        val itemView = inflater.inflate(R.layout.card_rec, parent, false)
        return ReccViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReccViewHolder, position: Int) {
        if (recs != null) {
            val current = recs[position]
            Glide.with(context).load(current.url).override(width, height / 4).centerCrop().into(holder.image)
            holder.nameTv.text = current.name
            holder.descTv.text = current.description
            holder.itemView.setOnClickListener {
                val launchMain = Intent(context, MainActivity::class.java)
                launchMain.putExtra(MainActivity.SAVED, current.name)
                launchMain.putExtra(MainActivity.OVERRIDE, true)
                val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", current.name)
                assert(clipboard != null)
                clipboard!!.setPrimaryClip(clip)
                Toast.makeText(context, "Saved to clipboard", Toast.LENGTH_SHORT).show()
                context.startActivity(launchMain)
            }

            holder.itemView.setOnLongClickListener {
                val defaultConfirm = AlertDialog.Builder(context)
                defaultConfirm.setTitle("Set as default?")
                defaultConfirm.setMessage("Are you sure you want " + current.name + " to be your default subreddit?")
                defaultConfirm.setPositiveButton("Yes") { _, _ ->
                    val preferences = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
                    val prefEdit = preferences.edit()
                    prefEdit.putString(SettingsActivity.DEFAULT, current.name)
                    prefEdit.apply()
                    Toast.makeText(context, "Set " + current.name + " as default", Toast.LENGTH_SHORT).show()
                }
                defaultConfirm.setNegativeButton("No")
                { _, _ -> Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show() }
                defaultConfirm.show()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return recs!!.size
    }


    inner class ReccViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.rec_image)
        var nameTv: TextView = view.findViewById(R.id.rec_name)
        var descTv: TextView = view.findViewById(R.id.rec_desc)

    }
}
