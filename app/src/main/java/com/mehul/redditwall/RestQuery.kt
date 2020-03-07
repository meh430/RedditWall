package com.mehul.redditwall

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutionException

//opens up https connection to get json data and return as a string
internal class RestQuery(private val QUERY: String, private val context: Context?, private val images: ArrayList<BitURL>?,
                         private val adapter: ImageAdapter?, private val progress: ProgressBar?,
                         private val imageTask: AsyncTask<String, Void, Void>) {
    private var sort: Int = 0

    fun getQueryJson(first: Boolean): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        var jsonString = ""

        try {
            val MODIFIER: String
            val AFTER: String
            //https://www.reddit.com/r/memes/top/.json?t=all
            val preferences = context?.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
            sort = preferences!!.getInt(SettingsActivity.SORT_METHOD, MainActivity.HOT)
            when (sort) {
                MainActivity.HOT -> {
                    MODIFIER = "/hot"
                    AFTER = MainActivity.AFTER_HOT
                }
                MainActivity.NEW -> {
                    MODIFIER = "/new"
                    AFTER = MainActivity.AFTER_NEW
                }
                MainActivity.TOP -> {
                    MODIFIER = "/top"
                    AFTER = MainActivity.AFTER_TOP
                }
                else -> {
                    AFTER = ""
                    MODIFIER = ""
                }
            }
            val BASE = "https://www.reddit.com/r/"
            val queryBuild = StringBuilder(BASE)
            val END = "/.json"
            if (first) {
                queryBuild.append(QUERY)
                queryBuild.append(MODIFIER)
                queryBuild.append(END)
                if (MODIFIER.contains("top")) {
                    queryBuild.append("?t=all")
                }
            } else {
                queryBuild.append(QUERY)
                queryBuild.append(MODIFIER)
                queryBuild.append(END)
                if (MODIFIER.contains("top")) {
                    queryBuild.append("?t=all&&after=")
                } else {
                    queryBuild.append("?after=")
                }
                queryBuild.append(AFTER)
            }

            Log.e("URL", queryBuild.toString())

            val requestURL = URL(queryBuild.toString())

            urlConnection = requestURL.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            val inputStream = urlConnection.inputStream
            reader = BufferedReader(InputStreamReader(inputStream))
            val builder = StringBuilder()

            var line: String? = reader.readLine()
            while (line != null) {
                if (imageTask.isCancelled) {
                    break
                }
                builder.append(line)
                builder.append("\n")
                line = reader.readLine()
            }

            if (imageTask.isCancelled) {
                return null
            }

            if (builder.isEmpty()) {
                return null
            }

            jsonString = builder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        Log.d("JSON", jsonString)
        return jsonString
    }

    fun getImages(jsonResult: String) {
        val scale = (context?.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE)
        !!.getInt(SettingsActivity.LOAD_SCALE, 2) + 1) * 2
        if (imageTask.isCancelled) {
            return
        }
        //TODO: change with lite mode
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        try {
            var json = JSONObject(jsonResult)
            json = json.getJSONObject("data")
            when (sort) {
                MainActivity.HOT -> MainActivity.AFTER_HOT = json.getString("after")
                MainActivity.NEW -> MainActivity.AFTER_NEW = json.getString("after")
                MainActivity.TOP -> MainActivity.AFTER_TOP = json.getString("after")
            }
            val childrenArr = json.getJSONArray("children")

            for (i in 0 until childrenArr.length()) {
                if (imageTask.isCancelled) {
                    return
                }
                val curr = childrenArr.getJSONObject(i)
                val data = curr.getJSONObject("data")
                if (!data.has("preview")) {
                    continue
                }
                val preview = data.getJSONObject("preview")
                val image = preview.getJSONArray("images").getJSONObject(0)
                var gif: JSONObject? = null
                var isImage = true
                val canLoadGif = context.getSharedPreferences(MainActivity.SharedPrefFile, Context.MODE_PRIVATE).getBoolean(SettingsActivity.LOAD_GIF, true)
                if (canLoadGif) {
                    if (image.has("variants") && image.getJSONObject("variants").has("gif")) {
                        isImage = false
                        gif = image.getJSONObject("variants").getJSONObject("gif")
                    }
                }

                val source: JSONObject
                source = if (isImage) {
                    image.getJSONObject("source")
                } else {
                    gif!!.getJSONObject("source")
                }

                try {
                    val url = source.getString("url").replace("amp;".toRegex(), "")
                    if (isImage) {
                        val bitmap = Glide.with(context).asBitmap().load(url).override(width / scale, height / 4).centerCrop().submit().get()
                        //Bitmap bitmap = Picasso.get().load(url).resize(width / 12, height / 4).centerCrop().get();

                        images?.add(BitURL(bitmap, url))
                    } else {
                        //Bitmap bitmap = Glide.with(context).asBitmap().load(url).override(width / 2, 500).centerCrop().submit().get();
                        images?.add(BitURL(null, url))
                    }
                    context.runOnUiThread {
                        adapter?.notifyDataSetChanged()
                        progress?.visibility = View.GONE
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
